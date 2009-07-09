/*
 * JBoss DNA (http://www.jboss.org/dna)
 * See the COPYRIGHT.txt file distributed with this work for information
 * regarding copyright ownership.  Some portions may be licensed
 * to Red Hat, Inc. under one or more contributor license agreements.
 * See the AUTHORS.txt file in the distribution for a full listing of 
 * individual contributors.
 *
 * JBoss DNA is free software. Unless otherwise indicated, all code in JBoss DNA
 * is licensed to you under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 * 
 * JBoss DNA is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.dna.jcr;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.stub;
import java.io.IOException;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Workspace;
import org.jboss.dna.graph.ExecutionContext;
import org.jboss.dna.graph.Graph;
import org.jboss.dna.graph.connector.RepositoryConnection;
import org.jboss.dna.graph.connector.RepositoryConnectionFactory;
import org.jboss.dna.graph.connector.RepositorySourceException;
import org.jboss.dna.graph.connector.inmemory.InMemoryRepositorySource;
import org.jboss.dna.graph.property.Name;
import org.jboss.dna.graph.property.Path;
import org.junit.Before;
import org.junit.BeforeClass;

/**
 * 
 */
public abstract class AbstractJcrTest {

    protected static ExecutionContext context;
    protected static RepositoryNodeTypeManager rntm;
    protected InMemoryRepositorySource source;
    protected Graph store;
    protected int numberOfConnections;
    protected SessionCache cache;
    protected JcrSession jcrSession;
    protected JcrNodeTypeManager nodeTypes;
    protected Workspace workspace;
    protected Repository repository;

    /**
     * Initialize the expensive activities, and in particular the RepositoryNodeTypeManager instance.
     * 
     * @throws Exception
     */
    @BeforeClass
    public static void beforeAll() throws Exception {
        context = new ExecutionContext();

        // Create the node type manager ...
        context.getNamespaceRegistry().register(Vehicles.Lexicon.Namespace.PREFIX, Vehicles.Lexicon.Namespace.URI);
        rntm = new RepositoryNodeTypeManager(context);
        try {
            rntm.registerNodeTypes(new CndNodeTypeSource(new String[] {"/org/jboss/dna/jcr/jsr_170_builtins.cnd",
                "/org/jboss/dna/jcr/dna_builtins.cnd"}));
            rntm.registerNodeTypes(new NodeTemplateNodeTypeSource(Vehicles.getNodeTypes(context)));
        } catch (RepositoryException re) {
            re.printStackTrace();
            throw new IllegalStateException("Could not load node type definition files", re);
        } catch (IOException ioe) {
            ioe.printStackTrace();
            throw new IllegalStateException("Could not access node type definition files", ioe);
        }
    }

    /**
     * Set up and initialize the store and session. This allows each test method to be independent; any changes made to the
     * sessions or store state will not be seen by other tests.
     * 
     * @throws Exception
     */
    @Before
    public void beforeEach() throws Exception {
        // Set up the store ...
        source = new InMemoryRepositorySource();
        source.setName("store");
        // Use a connection factory so we can count the number of connections that were made
        RepositoryConnectionFactory connectionFactory = new RepositoryConnectionFactory() {
            public RepositoryConnection createConnection( String sourceName ) throws RepositorySourceException {
                if (source.getName().equals(sourceName)) {
                    ++numberOfConnections;
                    return source.getConnection();
                }
                return null;
            }
        };
        store = Graph.create(source.getName(), connectionFactory, context);

        // Load the store with content ...
        String xmlResourceName = getResourceNameOfXmlFileToImport();
        store.importXmlFrom(AbstractJcrTest.class.getClassLoader().getResourceAsStream(xmlResourceName)).into("/");
        numberOfConnections = 0; // reset the number of connections

        nodeTypes = new JcrNodeTypeManager(context, rntm);

        // Stub the session ...
        jcrSession = mock(JcrSession.class);
        stub(jcrSession.nodeTypeManager()).toReturn(nodeTypes);

        cache = new SessionCache(jcrSession, store.getCurrentWorkspaceName(), context, nodeTypes, store);

        workspace = mock(Workspace.class);
        repository = mock(Repository.class);
        stub(jcrSession.getWorkspace()).toReturn(workspace);
        stub(jcrSession.getRepository()).toReturn(repository);
        stub(workspace.getName()).toReturn("workspace1");
    }

    protected String getResourceNameOfXmlFileToImport() {
        return "cars.xml";
    }

    protected Name name( String name ) {
        return context.getValueFactories().getNameFactory().create(name);
    }

    protected Path relativePath( String relativePath ) {
        return context.getValueFactories().getPathFactory().create(relativePath);
    }

    protected Path path( String absolutePath ) {
        return context.getValueFactories().getPathFactory().create(absolutePath);
    }
}