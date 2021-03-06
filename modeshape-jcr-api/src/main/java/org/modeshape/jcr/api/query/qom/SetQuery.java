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
package org.modeshape.jcr.api.query.qom;

/**
 * Represents a set query.
 */
public interface SetQuery extends QueryCommand {
    /**
     * Get the left-hand query.
     * 
     * @return the left-hand query; never null
     */
    public QueryCommand getLeft();

    /**
     * Get the right-hand query.
     * 
     * @return the right-hand query; never null
     */
    public QueryCommand getRight();

    /**
     * Get the set operation for this query.
     * 
     * @return the operation; never null
     */
    public String getOperation();

    /**
     * Return whether this set query is a 'UNION ALL' or 'INTERSECT ALL' or 'EXCEPT ALL' query.
     * 
     * @return true if this is an 'ALL' query, or false otherwise
     */
    public boolean isAll();
}
