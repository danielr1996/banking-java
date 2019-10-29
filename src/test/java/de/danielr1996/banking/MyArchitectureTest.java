package de.danielr1996.banking;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.runner.RunWith;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

@AnalyzeClasses(packages = "de.danielr1996.banking")
public class MyArchitectureTest {

  @ArchTest
  public static final ArchRule myRule = classes()
    .that().resideInAPackage("..domain..")
    .should().onlyBeAccessed().byAnyPackage("..application..", "..service..");

}
