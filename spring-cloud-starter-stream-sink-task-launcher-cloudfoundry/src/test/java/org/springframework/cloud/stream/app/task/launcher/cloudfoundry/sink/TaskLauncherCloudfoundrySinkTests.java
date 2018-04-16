/*
 * Copyright 2016-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.cloud.stream.app.task.launcher.cloudfoundry.sink;

import com.github.zafarkhaja.semver.Version;
import org.cloudfoundry.client.CloudFoundryClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.deployer.spi.cloudfoundry.CloudFoundryConnectionProperties;
import org.springframework.cloud.deployer.spi.cloudfoundry.CloudFoundryDeploymentProperties;
import org.springframework.cloud.deployer.spi.task.TaskLauncher;
import org.springframework.context.annotation.Bean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

/**
 * Test Properties for the Task Launcher Cloud Foundry Sink.
 *
 * @author Glenn Renfro
 * @author Thomas Risberg
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext
public abstract class TaskLauncherCloudfoundrySinkTests {

	@Autowired
	protected CloudFoundryConnectionProperties cloudFoundryConnectionProperties;

	@Autowired
	protected CloudFoundryDeploymentProperties taskDeploymentProperties;

	@TestPropertySource(properties = { "spring.cloud.deployer.cloudfoundry.url = http://hello", "spring.cloud.deployer.cloudfoundry.password = bar",
			"spring.cloud.deployer.cloudfoundry.username = foo", "spring.cloud.deployer.cloudfoundry.space = space",
			"spring.cloud.deployer.cloudfoundry.org = org", "spring.cloud.deployer.cloudfoundry.domain=baz",
			"spring.cloud.deployer.cloudfoundry.services=mydb", "spring.cloud.deployer.cloudfoundry.apiTimeout=123"})
	public static class PropertiesPopulatedTests extends TaskLauncherCloudfoundrySinkTests {

		@Test
		public void test() throws Exception {
			assertEquals("http://hello", cloudFoundryConnectionProperties.getUrl().toString());
			assertEquals("bar", cloudFoundryConnectionProperties.getPassword());
			assertEquals("foo", cloudFoundryConnectionProperties.getUsername());
			assertEquals("org", cloudFoundryConnectionProperties.getOrg());
			assertEquals("space", cloudFoundryConnectionProperties.getSpace());

			assertEquals("baz", taskDeploymentProperties.getDomain());
			assertEquals(123, taskDeploymentProperties.getApiTimeout());
			assertTrue(taskDeploymentProperties.getServices().contains("mydb"));
		}
	}

	@SpringBootApplication
	public static class TaskLauncherSinkApplication {

		@Bean
		public TaskLauncher taskLauncher() {
			return mock(TaskLauncher.class);
		}

		@Bean
		public Version version(CloudFoundryClient client) {
			return Version.forIntegers(1, 2, 3);
		}
	}
}
