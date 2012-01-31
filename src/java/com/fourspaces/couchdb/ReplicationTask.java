/**
 * Copyright (c) 2011 Cummings Engineering Consultants, Inc. 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * 		http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */
package com.fourspaces.couchdb;

import java.net.InetAddress;
import java.net.URL;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This class encapsulates the data for a replication task running on a couch server 
 * (as returned from the query to "_active_tasks")
 * 
 * Note: The "equals()" method is overidden for this class in order to easily compare two tasks. However the
 * "hashCode()" method was not overidden, so this class should not be used inside hash tables.
 * 
 * @author anthony.payne
 *
 */
public class ReplicationTask extends CouchTask {
	
	/**
	 * Logging instance for this class.
	 */
	static Log log = LogFactory.getLog(Document.class);
	
	/** Used to identify a running task as a replication task */
	public static final String TASK_TYPE = "Replication";
	
	/** Used to parse the string holding the task details */
	private static final String DELIMITER = " ";
	
	/** JSON key for the source field */
	private static final String SOURCE_KEY = "source";
	/** JSON key for the target field */
	private static final String TARGET_KEY = "target";
	/** JSON key for the create_target field */
	private static final String CREATE_TARGET_KEY = "create_target";
	/** JSON key for the continuous field */
	private static final String CONTINUOUS_KEY = "continuous";
	/** JSON key for the cancel field */
	private static final String CANCEL_KEY = "cancel";
	
	/** Source (DB) of the replication */
	private ReplicationTarget source;
	
	/** Target (DB) of the replication */
	private ReplicationTarget destination;
	
	/** Flag indicating if this is to be a continuous replication or not */
	private boolean continuous;
	/** Flag indicating if the target (DB) should be created if it does not exist */
	private boolean createTarget;
	/** Flag indicating if this task is to be canceled or not */
	private boolean cancel;
	
	/**
	 * Creates a replication task from the task details returned from a DB query about running tasks.
	 * 
	 * @param task Task details
	 * @param status Status of task
	 * @param pid PID of task
	 */
	public ReplicationTask(final String task, final String status, final String pid) {
		super(TASK_TYPE, task, status, pid);
		
		source = null;
		destination = null;
		continuous = false;
		createTarget = false;
		cancel = false;
	}
	
	/**
	 * Creates a replication task between the source and destination.
	 * 
	 * @param source Source for the replication 
	 * @param destination Target for the replication
	 */
	public ReplicationTask(ReplicationTarget source, ReplicationTarget destination) {
		super(TASK_TYPE, null, null, null);
		
		this.source = source;
		this.destination = destination;
	}

	/**
	 * Initializes the fields based on the data in the "task" string.
	 * @return
	 */
	public boolean loadDetailsFromTask() {
		if(task == null) {
			return false;
		}
		
		String[] parts = task.split(DELIMITER);
		
		if(parts.length < 4) {
			log.error("Unable to parse replication task: " + task);
			return false;
		}
		
		// [0] - Task ID
		// [1] - source URL
		// [2] - "->"
		// [3] - destination URL
		
		source = ReplicationTarget.fromUrl(parts[1]);
		destination = ReplicationTarget.fromUrl(parts[3]);
		
		if(source == null || destination == null) {
			log.error("Unable to extract source and destination details from replication task: " + task);
			return false;
		}
		return true;
	}

	/**
	 * @return the source
	 */
	public ReplicationTarget getSource() {
		return source;
	}

	/**
	 * @return the destination
	 */
	public ReplicationTarget getDestination() {
		return destination;
	}

	/**
	 * @return the continuous
	 */
	public boolean isContinuous() {
		return continuous;
	}
	
	/**
	 * @return The JSON object representing this replication task. Null is returned upon failure.
	 */
	public JSONObject getCreateRequest() {
		final JSONObject object = new JSONObject();
		final String source = this.source.buildUrl();
		final String destination = this.destination.buildUrl();
		if(source == null || destination == null) {
			log.error("Unable to build source or destination URL");
			return null;
		}
		object.put(SOURCE_KEY, source);
		object.put(TARGET_KEY, destination);
		
		if(createTarget) {
			object.put(CREATE_TARGET_KEY, Boolean.TRUE);
		}
		
		if(continuous) {
			object.put(CONTINUOUS_KEY, Boolean.TRUE);
		}
		
		if(cancel) {
			object.put(CANCEL_KEY, Boolean.TRUE);
		}
		
		return object;
	}

	/**
	 * sets the continuous flag. 
	 */
	public void setContinuous() {
		continuous = true;
	}

	/**
	 * Sets the create target flag 
	 */
	public void setCreateTarget() {
		createTarget = true;
	}

	/**
	 * Sets the cancel flag
	 */
	public void setCancel() {
		cancel = true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof ReplicationTask) {
			final ReplicationTask other = (ReplicationTask) obj;
			if(source.equals(other.source) && destination.equals(other.destination)) {
				return true;
			}
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		throw new RuntimeException("hashCode() is not supported yet.");
	}
	
	/**
	 * Simple class for encapsulating the details about a source or destination of a replication task.
	 * 
	 * Note: The "equals()" method is overidden for this class in order to easily compare two targets. However the
	 * "hashCode()" method was not overidden, so this class should not be used inside hash tables.
	 * 
	 * @author anthony.payne
	 *
	 */
	public static class ReplicationTarget {
		private final static String FULL_URL_PREFIX = "http";
		private final static String PATH_SEPARATOR = "/";
		private final static int PORT_NOT_USED = -1;
		
		/** The item in the database being replicated (could be a DB or a document) */
		private String replicatedEntity;
		
		/** The server */
		private String server;
		
		/** The Couch port */
		private int port;
		
		/** Indicates if this target is remote (true) or local (false) */
		private boolean isRemote;
		
		private ReplicationTarget() {
			
		}

		/**
		 * Constructor used for local targets
		 * @param replicatedEntry
		 */
		public ReplicationTarget(final String replicatedEntry) {
			this(replicatedEntry, null, PORT_NOT_USED);
		}
		
		/**
		 * Constructor used to specify the server and port along with the entity being replicated.
		 * @param replicatedEntry
		 * @param server
		 * @param port
		 */
		public ReplicationTarget(final String replicatedEntry, final String server, final int port) {
			this.replicatedEntity = replicatedEntry;
			this.server = server;
			this.port = port;
			isRemote = (server != null);
		}
		
		/**
		 * Builds the URL for this replication source or destination
		 * @return String holding the URL
		 */
		public String buildUrl() {
			final StringBuffer buffer = new StringBuffer();
			
			if(isRemote == false) {
				buffer.append(replicatedEntity);
			} else {
				buffer.append(FULL_URL_PREFIX + "://" + server + ":" + port + "/" + replicatedEntity);				
			}
			
			return buffer.toString();
		}
		
		/**
		 * Constructs a ReplicationTarget given a URL
		 * @param url URL from a replication task
		 * @return The replication target instance if successful; null if not
		 */
		static private ReplicationTarget fromUrl(final String url) {
			if(url.startsWith(FULL_URL_PREFIX) == false) {
				final ReplicationTarget target = new ReplicationTarget();
				target.isRemote = false;
				target.port = PORT_NOT_USED;
				target.replicatedEntity = url;
				target.server = null;
				return target;
			}
			
			try {
				final URL asUrl = new URL(url);
				final ReplicationTarget target = new ReplicationTarget();
				target.server = asUrl.getHost();
				target.port = asUrl.getPort();
				target.replicatedEntity = asUrl.getPath();
				
				if(target.replicatedEntity.startsWith(PATH_SEPARATOR)) {
					target.replicatedEntity = target.replicatedEntity.substring(PATH_SEPARATOR.length());
				}
				if(target.replicatedEntity.endsWith(PATH_SEPARATOR)) {
					target.replicatedEntity = target.replicatedEntity.substring(0, target.replicatedEntity.length() - 
							PATH_SEPARATOR.length());
				}
				
				target.isRemote = false;
				
				if(target.server != null) {
					final InetAddress tempAddress = InetAddress.getByName(target.server);
					target.isRemote = !(tempAddress.isLoopbackAddress());
				}
				
				log.debug(target.toString());
				
				return target;
			} catch(final Exception e) {
				log.debug("Failed to create target due to exception, " + e);
			}
			return null;
		}


		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			StringBuffer buffer = new StringBuffer();
			buffer.append("Host: ");
			if(server != null) {
				buffer.append(server + ", ");
			} else {
				buffer.append("Not set, ");
			}
			buffer.append("Port = " + port + ", ");
			buffer.append("Path = " + replicatedEntity + ", isRemote = " + isRemote);
			return buffer.toString();
		}


		/**
		 * @return the replicatedEntity
		 */
		public String getReplicatedEntity() {
			return replicatedEntity;
		}


		/**
		 * @return the sourceDatabaseServer
		 */
		public String getServer() {
			return server;
		}


		/**
		 * @return the sourceDatabasePort
		 */
		public int getPort() {
			return port;
		}


		/**
		 * @return the isRemote
		 */
		public boolean isRemote() {
			return isRemote;
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object obj) {
			if(obj instanceof ReplicationTarget) {
				final ReplicationTarget other = (ReplicationTarget) obj;
				
				if(((replicatedEntity == null && other.replicatedEntity == null) || 
						replicatedEntity.equals(other.replicatedEntity)) && 
						((server == null && other.server == null) || server.equals(other.server)) &&
						port == other.port) {
					return true;
				}
			}
			return false;
		}
		
		/* (non-Javadoc)
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			throw new RuntimeException("hashCode() is not supported yet.");
		}
	}
}
