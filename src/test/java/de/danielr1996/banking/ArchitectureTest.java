package de.danielr1996.banking;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

@AnalyzeClasses(packages = "de.danielr1996.banking")
public class ArchitectureTest {

  /*@ArchTest
  public static final ArchRule myRule = classes()
    .that().resideInAPackage("..domain..")
    // FIX: Remove auth
    .should().onlyBeAccessed().byAnyPackage("..application..","..auth..");*/

  @ArchTest
  public static final ArchRule layers = layeredArchitecture()
    .layer("Application").definedBy("..application..")
    .layer("Infrastructure").definedBy("..infrastructure..")
    .layer("Domain").definedBy("..domain..")
    // FIX: Remove auth
    .layer("Auth").definedBy("..auth..")
    .whereLayer("Infrastructure").mayNotBeAccessedByAnyLayer()
    .whereLayer("Application").mayOnlyBeAccessedByLayers("Infrastructure")
    .whereLayer("Domain").mayOnlyBeAccessedByLayers("Application","Infrastructure","Auth");

}
