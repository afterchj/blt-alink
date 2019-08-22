package com.tpadsz.after.entity.time;

import java.util.List;

/**
 * @program: blt-alink
 * @description:
 * @author: Mr.Ma
 * @create: 2019-08-22 14:41
 **/
public class ProjectTimer {

    private Integer projectId;
    private String projectName;
    private List<MeshTimer> meshList;

    public List<MeshTimer> getMeshList() {
        return meshList;
    }

    public void setMeshList(List<MeshTimer> meshList) {
        this.meshList = meshList;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    @Override
    public String toString() {
        return "ProjectTimer{" +
                "projectId=" + projectId +
                ", projectName='" + projectName + '\'' +
                ", meshList=" + meshList +
                '}';
    }
}
