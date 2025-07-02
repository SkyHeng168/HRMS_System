package com.cotech.systemcoreapi.controller;

import com.cotech.systemcoreapi.dto.ProjectDto.ProjectRequest;
import com.cotech.systemcoreapi.dto.ProjectDto.ProjectRespond;
import com.cotech.systemcoreapi.service.impl.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/version1/project")
public class ProjectController {
    private final ProjectService projectService;

    @GetMapping
    public ResponseEntity<List<ProjectRequest>> getAllProjects() {
        return new ResponseEntity<>(projectService.getAllProjects(), HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<ProjectRequest> getProjectById(
            @PathVariable Long id
    ) {
        ProjectRequest projectRequest = projectService.getProjectById(id);
        return ResponseEntity.status(HttpStatus.OK).body(projectRequest);
    }

    @PostMapping
    public ResponseEntity<ProjectRespond> createProject(
            @RequestBody ProjectRespond projectRespond
    ) {
        ProjectRespond newProjectRespond = projectService.createProject(projectRespond);
        return ResponseEntity.status(HttpStatus.CREATED).body(newProjectRespond);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ProjectRespond> updateProject(
            @PathVariable Long id,
            @RequestBody ProjectRespond projectRespond
    ){
        ProjectRespond updateProject = projectService.updateProject(projectRespond, id);
        return ResponseEntity.status(HttpStatus.OK).body(updateProject);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProject(
            @PathVariable Long id
    ){
        projectService.deleteProject(id);
        return ResponseEntity.status(HttpStatus.OK).body("Project deleted successfully");
    }
}
