/*
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.xwiki.test.ldap;

import java.lang.reflect.Method;

import org.xwiki.test.ldap.framework.LDAPTestSetup;
import org.xwiki.test.ldap.framework.XWikiLDAPTestSetup;
import org.xwiki.test.selenium.framework.XWikiSeleniumTestSetup;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * A class listing all the Functional tests to execute. We need such a class (rather than letting the JUnit Runner
 * discover the different TestCases classes by itself) because we want to start/stop XWiki before and after the tests
 * start (but only once).
 * 
 * @version $Id$
 */
public class AllTests extends TestCase
{
    /**
     * The pattern to filter the tests to launch.
     */
    private static final String PATTERN = ".*" + System.getProperty("pattern", "");

    /**
     * @return the test suite.
     * @throws Exception error when creation the tests suite.
     */
    public static Test suite() throws Exception
    {
        TestSuite suite = new TestSuite();

        // TODO: I don't like listing tests here as it means we can add a new TestCase class and
        // forget to add it here and the tests won't be run but we'll not know about it and we'll
        // think the tests are all running fine. I haven't found a simple solution to this yet
        // (there are complex solutions like searching for all tests by parsing the source tree).
        // I think there are TestSuite that do this out there but I haven't looked for them yet.

        // Unit tests
        addTestCase(suite, XWikiLDAPUtilsTest.class);
        addTestCase(suite, XWikiLDAPConnectionTest.class);
        addTestCase(suite, XWikiLDAPAuthServiceImplTest.class);

        // Selenium tests
        TestSuite seleniumSuite = new TestSuite();

        addTestCaseSuite(seleniumSuite, LDAPAuthTest.class);

        suite.addTest(new XWikiSeleniumTestSetup(new XWikiLDAPTestSetup(seleniumSuite)));

        return new LDAPTestSetup(suite);
    }

    private static void addTestCase(TestSuite suite, Class< ? > testClass) throws Exception
    {
        if (testClass.getName().matches(PATTERN)) {
            suite.addTest(new TestSuite(testClass));
        }
    }

    private static void addTestCaseSuite(TestSuite suite, Class< ? > testClass) throws Exception
    {
        if (testClass.getName().matches(PATTERN)) {
            Method method = testClass.getMethod("suite");
            suite.addTest((Test) method.invoke(null));
        }
    }
}
