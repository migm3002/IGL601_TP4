package ca.udes.tp;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import ca.udes.tp.dao.TestPariDAO;
import ca.udes.tp.object.MatchTest;
import integrationTests.TestIntegrationMatchBDD;

@RunWith(Suite.class)
@SuiteClasses(
		{
			MatchTest.class,
			TestIntegrationMatchBDD.class,
			TestPariDAO.class
		}
	)
public class TestSuite {

}
