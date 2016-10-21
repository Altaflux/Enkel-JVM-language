package com.kubadziworski.domain.resolver;

import java.util.List;
import java.util.Optional;


public interface BaseImportResolver {

    List<DeclarationDescriptor> extractClazzFieldOrMethods(String importPackage);

    Optional<List<DeclarationDescriptor>> getMethodsOrFields(String importPackage);

    List<DeclarationDescriptor> extractClassesFromPackage(String importPackage);


    default ClassEntity splitDeclaration(String importPackage) {
        if (!importPackage.contains(".")) {
            return new ClassEntity("", importPackage);
        }

        String methodOrField = importPackage.substring(importPackage.lastIndexOf('.') + 1);
        String importString = importPackage.substring(0, importPackage.lastIndexOf('.'));

        return new ClassEntity(importString, methodOrField);
    }

    class ClassEntity {

        final String packageName;
        final String clazzName;

        private ClassEntity(String packageName, String clazzName) {
            this.packageName = packageName;
            this.clazzName = clazzName;
        }
    }
}
