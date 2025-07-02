package com.cotech.systemcoreapi.service.impl;

import com.cotech.systemcoreapi.dto.ProjectDto.ProjectRequest;
import com.cotech.systemcoreapi.dto.ProjectDto.ProjectRespond;
import com.cotech.systemcoreapi.exception.CustomException.NotFoundException;
import com.cotech.systemcoreapi.model.Employee;
import com.cotech.systemcoreapi.model.Project;
import com.cotech.systemcoreapi.repository.EmployeeRepository;
import com.cotech.systemcoreapi.repository.ProjectRepository;
import com.cotech.systemcoreapi.service.IProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectService implements IProjectService {
    private final ProjectRepository projectRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    public ProjectRespond createProject(ProjectRespond projectRespond) {
        System.out.println("Received project data: " + projectRespond);

        Optional<Project> existingProject = projectRepository.findByProjectName(projectRespond.projectName());

        if (existingProject.isPresent()) {
            throw new IllegalArgumentException("Project with the name '" + projectRespond.projectName() + "' already exists.");
        }

        Project project = Project.builder()
                .projectName(projectRespond.projectName())
                .projectDescription(projectRespond.projectDescription())
                .startDate(projectRespond.startDate())
                .endDate(projectRespond.endDate())
                .createAt(LocalDateTime.now())
                .employees(new ArrayList<>())
                .build();

        Project savedProject = projectRepository.save(project);
        System.out.println("Saved project: " + savedProject);

        if (projectRespond.employeeId() != null && !projectRespond.employeeId().isEmpty()) {
            List<Employee> employees = employeeRepository.findAllById(projectRespond.employeeId());
            savedProject.setEmployees(employees);
            projectRepository.save(savedProject);
            System.out.println("Updated project with employees: " + savedProject);
        }

        return new ProjectRespond(
                savedProject.getId(),
                savedProject.getProjectName(),
                savedProject.getProjectDescription(),
                savedProject.getStartDate(),
                savedProject.getEndDate(),
                savedProject.getEmployees().stream().map(Employee::getId).collect(Collectors.toList()),
                savedProject.getCreateAt(),
                savedProject.getUpdateAt()
        );
    }

    @Override
    public List<ProjectRequest> getAllProjects() {
        List<Project> projects = projectRepository.findAll();
        if (projects.isEmpty()) {
            throw new NotFoundException("No projects found");
        }
        return projects.stream()
                .map(project -> new ProjectRequest(
                        project.getId(),
                        project.getProjectName(),
                        project.getProjectDescription(),
                        project.getEmployees().stream()
                                .map(Employee::getProfilePicture)
                                .collect(Collectors.toList()),
                        project.getStartDate(),
                        project.getEndDate()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public ProjectRequest getProjectById(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(()-> new NotFoundException("Project Not "+ id ));
        return new ProjectRequest(
                project.getId(),
                project.getProjectName(),
                project.getProjectDescription(),
                project.getEmployees().stream()
                        .map(Employee::getProfilePicture)
                        .collect(Collectors.toList()),
                project.getStartDate(),
                project.getEndDate()
        );
    }

    @Override
    public ProjectRespond updateProject(ProjectRespond projectRespond, Long id) {
        Project existingProject = projectRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Project not found"));

        existingProject.setProjectName(projectRespond.projectName());
        existingProject.setProjectDescription(projectRespond.projectDescription());
        existingProject.setStartDate(projectRespond.startDate());
        existingProject.setEndDate(projectRespond.endDate());
        existingProject.setUpdateAt(LocalDateTime.now());

        List<Long> updatedEmployeeIds = projectRespond.employeeId();
        List<Employee> updatedEmployees = employeeRepository.findAllById(updatedEmployeeIds);

        existingProject.setEmployees(updatedEmployees);
        Project updatedProject = projectRepository.save(existingProject);
        List<Long> employeeIds = updatedProject.getEmployees().stream()
                .map(Employee::getId)
                .collect(Collectors.toList());

        return new ProjectRespond(
                updatedProject.getId(),
                updatedProject.getProjectName(),
                updatedProject.getProjectDescription(),
                updatedProject.getStartDate(),
                updatedProject.getEndDate(),
                employeeIds,
                updatedProject.getCreateAt(),
                updatedProject.getUpdateAt()
        );
    }

    @Override
    public void deleteProject(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(()-> new NotFoundException("Project not found" + id));
        projectRepository.delete(project);
    }
}
