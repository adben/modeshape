/*
 * ModeShape (http://www.modeshape.org)
 * See the COPYRIGHT.txt file distributed with this work for information
 * regarding copyright ownership.  Some portions may be licensed
 * to Red Hat, Inc. under one or more contributor license agreements.
 * See the AUTHORS.txt file in the distribution for a full listing of 
 * individual contributors. 
 *
 * ModeShape is free software. Unless otherwise indicated, all code in ModeShape
 * is licensed to you under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * ModeShape is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.modeshape.jboss.managed;

import org.jboss.managed.api.annotation.ManagementObject;
import org.modeshape.common.util.CheckArg;
import org.modeshape.jcr.JcrRepository;

/**
 * The <code>ManagedRepository</code> is a JBoss managed object for a
 * {@link JcrRepository repository}.
 */
@ManagementObject
public class ManagedRepository implements ModeShapeManagedObject {

	/**
	 * The name of this repository.
	 */
	private String name;

	/**
	 * Default constructor
	 */
	public ManagedRepository() {
		super();
		
	}

	/**
	 * Creates a JBoss managed object for the specified repository.
	 * 
	 * @param name the name of the repository being managed (never <code>null</code>)
	 */
	public ManagedRepository(String name) {
		CheckArg.isNotNull(name, "name");

		setName(name);
	}
	
	/**
	 * Set the name for this repository.
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Get the name for this repository.
	 * 
	 * @return String name
	 */
	public String getName() {
		return this.name;
	}

}
