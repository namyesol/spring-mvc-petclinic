package com.namyesol.petclinic.service;

import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"file:src/main/webapp/WEB-INF/application-context.xml"})
@ActiveProfiles("mybatis")
public class MyBatisClinicServiceIntegrationTest extends AbstractClinicServiceIntegrationTest {

}
