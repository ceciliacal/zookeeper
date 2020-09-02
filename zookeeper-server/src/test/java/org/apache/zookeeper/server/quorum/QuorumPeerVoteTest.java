package org.apache.zookeeper.server.quorum;

import org.junit.Test;

import static org.junit.Assert.assertFalse;

public class QuorumPeerVoteTest {


        @Test
        public void testIsNotLeaderBecauseNoVote() throws Exception {
            long localPeerId = 7;
            QuorumPeer peer = new QuorumPeer();
            peer.setId(localPeerId);
            peer.setCurrentVote(null);
            assertFalse(peer.isLeader(localPeerId));
        }

    
}


