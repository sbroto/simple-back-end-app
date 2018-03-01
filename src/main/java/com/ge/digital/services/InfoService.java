package com.ge.digital.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.ge.digital.model.*;

@Service
public class InfoService {

    @Value("${meta.project.name}")
    private String projectName;
    @Value("${meta.project.version}")
    private String projectVersion;
    @Value("${meta.project.build.date}")
    private String buildDate;
    @Value("${meta.git.commit.date}")
    private String commitDate;
    @Value("${meta.git.commit.id}")
    private String commitId;
    @Value("${meta.git.commit.author}")
    private String commitAuthor;
    @Value("${meta.git.commit.message}")
    private String commitMessage;


    public Info getInfo() {
        Info info = new Info();
        info.setProjectName(projectName);
        Version version = new Version();
        version.setVersion(projectVersion);
        version.setBuildDate(buildDate);
        version.setCommitDate(commitDate);
        version.setCommitId(commitId);
        version.setCommitAuthor(commitAuthor);
        version.setCommitMessage(commitMessage);
        info.setVersion(version);
        return info;
    }

}
