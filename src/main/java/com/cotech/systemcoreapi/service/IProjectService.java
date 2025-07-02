package com.cotech.systemcoreapi.service;

import com.cotech.systemcoreapi.dto.ProjectDto.ProjectRequest;
import com.cotech.systemcoreapi.dto.ProjectDto.ProjectRespond;

import java.util.List;

public interface IProjectService {
    ProjectRespond createProject(ProjectRespond projectRespond);
    List<ProjectRequest> getAllProjects();
    ProjectRequest getProjectById(Long id);
    ProjectRespond updateProject(ProjectRespond projectRespond, Long id);
    void deleteProject(Long id);
}
