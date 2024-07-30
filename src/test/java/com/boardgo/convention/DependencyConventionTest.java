package com.boardgo.convention;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeTests;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class DependencyConventionTest {

    JavaClasses javaClasses;

    @BeforeEach
    public void init() {
        javaClasses =
                new ClassFileImporter()
                        .withImportOption(new DoNotIncludeTests())
                        .importPackages("com.boardgo");
    }

    @Test
    @DisplayName("controller 이름을 가진 클래스는 자신만 의존된다")
    public void controller_이름을_가진_클래스는_자신만_의존된다() {
        classes().that().haveSimpleNameContaining("Controller")
                .should().onlyHaveDependentClassesThat().resideInAPackage("..controller..")
                .check(javaClasses);
    }


    @Test
    @DisplayName("service는 controller와 service에 의해서만 의존된다")
    public void service는_controller와_service에_의해서만_의존된다() {
        classes().that().resideInAPackage("..service..")
                .should().onlyHaveDependentClassesThat().resideInAnyPackage("..service..","..controller..")
                .check(javaClasses);
    }

    @Test
    @DisplayName("repository는 service에 의해서만 의존된다")
    public void repository는_service에_의해서만_의존된다() {
        classes().that().resideInAPackage("..repository..")
                .should().onlyHaveDependentClassesThat().resideInAnyPackage("..repository..","..service..")
                .check(javaClasses);
    }


}
