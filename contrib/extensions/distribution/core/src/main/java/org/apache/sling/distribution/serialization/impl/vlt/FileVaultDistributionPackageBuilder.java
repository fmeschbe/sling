/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.sling.distribution.serialization.impl.vlt;

import javax.jcr.Session;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.apache.jackrabbit.vault.fs.api.ImportMode;
import org.apache.jackrabbit.vault.fs.api.PathFilterSet;
import org.apache.jackrabbit.vault.fs.config.DefaultMetaInf;
import org.apache.jackrabbit.vault.fs.config.DefaultWorkspaceFilter;
import org.apache.jackrabbit.vault.fs.io.AccessControlHandling;
import org.apache.jackrabbit.vault.fs.io.ImportOptions;
import org.apache.jackrabbit.vault.packaging.ExportOptions;
import org.apache.jackrabbit.vault.packaging.Packaging;
import org.apache.jackrabbit.vault.packaging.VaultPackage;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.distribution.communication.DistributionRequest;
import org.apache.sling.distribution.event.impl.DistributionEventFactory;
import org.apache.sling.distribution.packaging.DistributionPackage;
import org.apache.sling.distribution.serialization.DistributionPackageBuilder;
import org.apache.sling.distribution.serialization.DistributionPackageBuildingException;
import org.apache.sling.distribution.serialization.DistributionPackageReadingException;
import org.apache.sling.distribution.serialization.impl.AbstractDistributionPackageBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * a {@link org.apache.sling.distribution.serialization.DistributionPackageBuilder} based on Apache Jackrabbit FileVault.
 * <p/>
 * Each {@link org.apache.sling.distribution.packaging.DistributionPackage} created by {@link FileVaultDistributionPackageBuilder} is
 * backed by a {@link org.apache.jackrabbit.vault.packaging.VaultPackage}. 
 */
public class FileVaultDistributionPackageBuilder extends AbstractDistributionPackageBuilder implements
        DistributionPackageBuilder {

    private static final String VERSION = "0.0.1";

    public static final String PACKAGING_TYPE = "vlt";

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final Packaging packaging;

    private ImportMode importMode;

    private AccessControlHandling aclHandling;

    public FileVaultDistributionPackageBuilder(Packaging packaging, DistributionEventFactory distributionEventFactory) {
        super(PACKAGING_TYPE, distributionEventFactory);
        this.packaging = packaging;
    }

    public FileVaultDistributionPackageBuilder(Packaging packaging, DistributionEventFactory distributionEventFactory, String importMode, String aclHandling) {
        super(PACKAGING_TYPE, distributionEventFactory);
        this.packaging = packaging;
        this.importMode = ImportMode.valueOf(importMode);
        this.aclHandling = AccessControlHandling.valueOf(aclHandling);
    }

    @Override
    protected DistributionPackage createPackageForAdd(ResourceResolver resourceResolver, DistributionRequest request)
            throws DistributionPackageBuildingException {
        Session session = null;
        try {
            session = getSession(resourceResolver);

            final String[] paths = request.getPaths();

            DefaultWorkspaceFilter filter = new DefaultWorkspaceFilter();
            DefaultMetaInf inf = new DefaultMetaInf();
            ExportOptions opts = new ExportOptions();
            for (String path : paths) {
                filter.add(new PathFilterSet(path));
            }
            inf.setFilter(filter);

            Properties props = new Properties();
            String packageGroup = "sling/distribution";
            props.setProperty(VaultPackage.NAME_GROUP, packageGroup);
            String packageName = String.valueOf(request.getTime());
            props.setProperty(VaultPackage.NAME_NAME, packageName);
            props.setProperty(VaultPackage.NAME_VERSION, VERSION);
            inf.setProperties(props);

            log.debug("assembling package {}", packageGroup + '/' + packageName + "-" + VERSION);

            opts.setMetaInf(inf);
            opts.setRootPath("/");
            File tmpFile = File.createTempFile("rp-vlt-create-" + System.nanoTime(), ".zip");
            VaultPackage vaultPackage = packaging.getPackageManager().assemble(session, opts, tmpFile);
            return new FileVaultDistributionPackage(vaultPackage);
        } catch (Exception e) {
            throw new DistributionPackageBuildingException(e);
        } finally {
            ungetSession(session);
        }
    }

    @Override
    protected DistributionPackage readPackageInternal(ResourceResolver resourceResolver, final InputStream stream)
            throws DistributionPackageReadingException {
        log.debug("reading a stream");
        DistributionPackage pkg = null;
        try {
            File tmpFile = File.createTempFile("rp-vlt-read-" + System.nanoTime(), ".zip");
            FileOutputStream fileStream = new FileOutputStream(tmpFile);
            IOUtils.copy(stream, fileStream);
            IOUtils.closeQuietly(fileStream);

            VaultPackage vaultPackage = packaging.getPackageManager().open(tmpFile);

            if (vaultPackage != null) {
                pkg = new FileVaultDistributionPackage(vaultPackage);
            } else {
                log.warn("stream could not be read as a vlt package");
            }

        } catch (Exception e) {
            throw new DistributionPackageReadingException("could not read / install the package", e);
        }
        return pkg;
    }


    @Override
    protected DistributionPackage getPackageInternal(ResourceResolver resourceResolver, String id) {
        DistributionPackage distributionPackage = null;
        try {
            File file = new File(id);
            if (file.exists()) {
                VaultPackage pkg = packaging.getPackageManager().open(file);
                distributionPackage = new FileVaultDistributionPackage(pkg);
            }
        } catch (Exception e) {
            log.warn("could not find a package with id : {}", id);
        }
        return distributionPackage;
    }


    @Override
    public boolean installPackageInternal(ResourceResolver resourceResolver, DistributionPackage distributionPackage) throws DistributionPackageReadingException {
        log.debug("reading a distribution package stream");

        Session session = null;
        try {
            session = getSession(resourceResolver);
            File file = new File(distributionPackage.getId());
            if (file.exists()) {
                VaultPackage pkg = packaging.getPackageManager().open(file);
                ImportOptions opts = new ImportOptions();
                if (aclHandling != null) {
                    opts.setAccessControlHandling(aclHandling);
                    log.debug("using acl handling {}", aclHandling);
                }
                else {
                    // default to overwrite
                    opts.setAccessControlHandling(AccessControlHandling.OVERWRITE);
                }
                if (importMode != null) {
                    opts.setImportMode(importMode);
                    log.debug("using import mode {}", importMode);
                }
                else {
                    // default to replace
                    opts.setImportMode(ImportMode.REPLACE);
                }
                pkg.extract(session, opts);
                return true;
            }
        } catch (Exception e) {
            log.error("could not read / install the package", e);
            throw new DistributionPackageReadingException(e);
        } finally {
            ungetSession(session);
        }
        return false;
    }
}
