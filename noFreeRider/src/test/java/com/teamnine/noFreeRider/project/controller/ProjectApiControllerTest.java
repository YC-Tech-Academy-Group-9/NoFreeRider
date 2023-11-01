package com.teamnine.noFreeRider.project.controller;

import com.teamnine.noFreeRider.project.repository.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@AutoConfigureMockMvc
public class ProjectApiControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    ProjectRepository projectRepository;

    @BeforeEach
    void mockMvcSetUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .build();
        projectRepository.deleteAll();
    }

    @DisplayName("프로젝트 추가 테스트")
    @Test
    void addProjectTest() throws Exception {
        // given
        final String url = "/projects";
        final String projectName = "name";
        final String summary = "sum";
        // 아직 로그인 관련 로직 구현 안되서 누락

    }
}
