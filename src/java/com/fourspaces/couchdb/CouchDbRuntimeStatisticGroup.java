/*
 * Copyright (c) 2012 Cummings Engineering Consultants.  All Rights Reserved.
 *
 * This software is proprietary to, and a valuable trade secret of, Cummings 
 * Engineering Consultants.
 *
 * The software and documentation may not be copied, reproduced, translated, 
 * or reduced to any electronic medium or machine-readable form without a 
 * prior written agreement from Cummings Engineering Consultants.
 * 
 *
 * UNLESS REQUIRED BY APPLICABLE LAW OR AGREED TO IN WRITING, THE SOFTWARE 
 * AND DOCUMENTATION ARE DISTRIBUTED ON AN "AS IS" BASIS, WITHOUT WARRANTIES 
 * OR CONDITIONS OF ANY KIND, EITHER EXPRESS OR IMPLIED INCLUDING BUT NOT 
 * LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR 
 * PURPOSE AND NONINFRINGEMENT.  REFER TO THE WRITTEN AGREEMENT FOR SPECIFIC 
 * LANGUAGE GOVERNING PERMISSIONS AND LIMITATIONS.
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