package com.github.motui.meicanintellijplugin.services

import com.github.motui.meicanintellijplugin.MyBundle
import com.intellij.openapi.project.Project

class MyProjectService(project: Project) {

    init {
        println(MyBundle.message("projectService", project.name))
    }
}
