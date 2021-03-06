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
package org.apache.sling.distribution.queue.impl.simple;

import org.apache.sling.distribution.queue.DistributionQueue;
import org.apache.sling.distribution.queue.DistributionQueueItem;
import org.apache.sling.distribution.queue.DistributionQueueItemState;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Testcase for {@link SimpleDistributionQueue}
 */
public class SimpleDistributionQueueTest {

    @Test
    public void testPackageAddition() throws Exception {
        DistributionQueue queue = new SimpleDistributionQueue("agentName", "default");
        DistributionQueueItem pkg = mock(DistributionQueueItem.class);
        assertTrue(queue.add(pkg));
        assertFalse(queue.isEmpty());
    }

    @Test
    public void testPackageAdditionAndRemoval() throws Exception {
        DistributionQueue queue = new SimpleDistributionQueue("agentName", "default");
        DistributionQueueItem pkg = mock(DistributionQueueItem.class);
        when(pkg.getId()).thenReturn("id");
        assertTrue(queue.add(pkg));
        assertFalse(queue.isEmpty());
        queue.remove(pkg.getId());
        assertTrue(queue.isEmpty());
        DistributionQueueItemState status = queue.getStatus(pkg);
        assertNotNull(status);
        assertTrue(status.isSuccessful());
    }

    @Test
    public void testPackageAdditionRetrievalAndRemoval() throws Exception {
        DistributionQueue queue = new SimpleDistributionQueue("agentName", "default");
        DistributionQueueItem pkg = mock(DistributionQueueItem.class);
        when(pkg.getId()).thenReturn("id");
        assertTrue(queue.add(pkg));
        assertFalse(queue.isEmpty());
        assertEquals(pkg, queue.getHead());
        assertFalse(queue.isEmpty());
        queue.remove(pkg.getId());
        assertTrue(queue.isEmpty());
        DistributionQueueItemState status = queue.getStatus(pkg);
        assertNotNull(status);
        assertTrue(status.isSuccessful());
        assertEquals(1, status.getAttempts());
    }

}
