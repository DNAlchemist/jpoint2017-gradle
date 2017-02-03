package ru.jpoint2017;

import groovy.lang.Closure;
import groovy.lang.DelegatesTo;
import groovy.lang.GroovyObjectSupport;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by ruslanmikhalev on 30/01/17.
 */
@SuppressWarnings("WeakerAccess")
public class Project extends GroovyObjectSupport implements PluginAware {

    Set<String> compilers = new HashSet<>();

    final File projectDir;
    private String sourceCompatibility;
    private RepositoryHandler repositoryHandler = new RepositoryHandler();
    private TaskHandler tasks = new TaskHandler();

    public Project(File projectDir) {
        this.projectDir = projectDir;
    }

    @Override
    public void apply(@DelegatesTo(ApplyConfig.class) Closure closure) {
        closure.setDelegate(new ApplyConfig(this));
        closure.call();
    }

    @Override
    public void apply(Action<? super ApplyConfig> action) {
        throw new UnsupportedOperationException("apply");
    }

    @Override
    public void apply(@DelegatesTo(ApplyConfig.class) Map<String, ?> options) {
        ApplyConfig applyConfig = new ApplyConfig(this);
        applyConfig.from((String)options.get("from"));

        if(options.get("plugin") instanceof String) {
            applyConfig.plugin((String)options.get("plugin"));
        } else
            applyConfig.plugin((Class<ApplyConfig.Plugin>)options.get("plugin"));
    }

    public File getProjectDir() {
        return projectDir;
    }

    public void setSourceCompatibility(String sourceCompatibility) {
        this.sourceCompatibility = sourceCompatibility;
    }

    public String getSourceCompatibility() {
        return sourceCompatibility;
    }

    public Set<MavenRepository> getRepositories() {
        return repositoryHandler.repositories;
    }

    public void repository(@DelegatesTo(RepositoryHandler.class) Closure closure) {
        closure.setDelegate(repositoryHandler);
        closure.call();
    }

    public void task(String name, @DelegatesTo(Task.class)Closure closure) {
        tasks.putTask(name, closure::call);
    }

    public TaskHandler getTasks() {
        return tasks;
    }
}
