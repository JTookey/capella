/*******************************************************************************
 * Copyright (c) 2006, 2020 THALES GLOBAL SERVICES.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *    Thales - initial API and implementation
 *******************************************************************************/
package org.polarsys.capella.test.validation.rules.ju.testcases.dwf_i;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.polarsys.capella.test.framework.api.BasicTestArtefact;
import org.polarsys.capella.test.framework.api.BasicTestSuite;

import junit.framework.Test;

public class DWFIRulesTestSuite extends BasicTestSuite {

	/**
	 * Returns the suite. This is required to unary launch this test.
	 */
	public static Test suite() {
		return new DWFIRulesTestSuite();
	}

	/**
	 * @see org.polarsys.capella.test.framework.api.BasicTestSuite#getTests()
	 */
	protected List<BasicTestArtefact> getTests() {
		List<BasicTestArtefact> tests = new ArrayList<BasicTestArtefact>();
		tests.add(new Rule_DWF_I_01());
		tests.add(new Rule_DWF_I_04());
		tests.add(new Rule_DWF_I_05());
		tests.add(new Rule_DWF_I_05bis());
		tests.add(new Rule_DWF_I_06());
		tests.add(new Rule_DWF_I_07());
		tests.add(new Rule_DWF_I_08());
		tests.add(new Rule_DWF_I_09());
		tests.add(new Rule_DWF_I_10());
		tests.add(new Rule_DWF_I_11());
		tests.add(new Rule_DWF_I_12());
		tests.add(new Rule_DWF_I_13());
		tests.add(new Rule_DWF_I_14());
		tests.add(new Rule_DWF_I_15());
		tests.add(new Rule_DWF_I_16());
		tests.add(new Rule_DWF_I_17());
		tests.add(new Rule_DWF_I_18());
		tests.add(new Rule_DWF_I_19());
		tests.add(new Rule_DWF_I_20());
		return tests;
	}

	@Override
	public List<String> getRequiredTestModels() {
		return Arrays.asList(new String[] {
				"RulesOnDesignTest", "Project_validation8" }); //$NON-NLS-1$
	}
}
