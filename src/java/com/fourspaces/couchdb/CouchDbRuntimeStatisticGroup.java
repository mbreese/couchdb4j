/*
 * Copyright (c) 2012 Cummings Engineering Consultants, Inc. 
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

/**
 * Data representation of a current set of runtime statistics from CouchDB. These values
 * come from the JSON response to a GET on /_stats.
 * 
 * @author jjones
 */
public class CouchDbRuntimeStatisticGroup {
	/**
	 * Description of this statistic; in a format provided by the underlying database. This may be null.
	 */
	public String description;

	/**
	 * Current value of this statistic. This may be null.
	 */
	public Integer current;

	/**
	 * Current sum of this statistic. This may be null.
	 */
	public Integer sum;

	/**
	 * Current minimum of this statistic. This may be null.
	 */
	public Integer min;

	/**
	 * Current maximum of this statistic. This may be null.
	 */
	public Integer max;

	/**
	 * Current mean of this statistic. This may be null.
	 */
	public Double mean;
	
	/**
	 * Current stddev of this statistic. This may be null.
	 */
	public Double stddev;
}