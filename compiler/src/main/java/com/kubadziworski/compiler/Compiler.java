package com.kubadziworski.compiler;

import com.kubadziworski.bytecodegeneration.BytecodeGenerator;
import com.kubadziworski.configuration.CompilerConfigInstance;
import com.kubadziworski.domain.CompilationUnit;
import com.kubadziworski.domain.scope.GlobalScope;
import com.kubadziworski.domain.type.ClassTypeFactory;
import com.kubadziworski.parsing.Parser;
import com.kubadziworski.validation.ARGUMENT_ERRORS;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.tools.ant.DirectoryScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


public class Compiler {

    private static final Logger LOGGER = LoggerFactory.getLogger(Compiler.class);


    public Compiler() {
        RadiumArguments radiumArguments = new RadiumArguments();
        radiumArguments.classLoader = ClassLoader.getSystemClassLoader();
        CompilerConfigInstance.initialize(radiumArguments);

    }
    public static void main(String[] args) throws Exception {
        try {
            new Compiler().compile(args);
        } catch (IOException exception) {
            LOGGER.error("ERROR: " + exception.getMessage());
        }
    }

    public void compile(String[] args) throws Exception {

        ClassTypeFactory.classLoader = ClassLoader.getSystemClassLoader();
        List<String> enkelFiles = getListOfFiles(args[0]);
        if (enkelFiles.isEmpty()) {
            LOGGER.error(ARGUMENT_ERRORS.NO_FILE.getMessage());
            return;
        }
        LOGGER.info("Files to compile: ");
        enkelFiles.forEach(LOGGER::info);
        GlobalScope globalScope = CompilerConfigInstance.getConfig().getGlobalScope();
        ClassTypeFactory.initialize(globalScope);

        Parser parser = new Parser(globalScope);
        List<Pair<String, List<String>>> compilationData = Collections
                .singletonList(Pair.of(Paths.get(".").toAbsolutePath().normalize().toString(), enkelFiles));
        List<CompilationUnit> compilationUnits = parser.processAllFiles(compilationData);

        compilationUnits.forEach(compilationUnit -> {
            LOGGER.info("Finished Parsing. Started compiling to bytecode.");
            try {
                saveBytecodeToClassFile(compilationUnit);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        ClassTypeFactory.initialize(null);
    }

    private List<String> getListOfFiles(String path) {

        DirectoryScanner scanner = new DirectoryScanner();

        scanner.setIncludes(new String[]{path});
        LOGGER.info("Base path: " + Paths.get(".").toAbsolutePath().normalize().toString());
        scanner.setBasedir(Paths.get(".").toAbsolutePath().normalize().toString());
        scanner.setCaseSensitive(true);
        scanner.scan();

        String[] files = scanner.getIncludedFiles();
        return Arrays.stream(files).filter(s -> s.endsWith(".enk"))
                .collect(Collectors.toList());
    }


    private void saveBytecodeToClassFile(CompilationUnit compilationUnit) throws IOException {
        BytecodeGenerator bytecodeGenerator = new BytecodeGenerator();
        List<BytecodeGenerator.GeneratedClassHolder> bytecode = bytecodeGenerator.generate(compilationUnit);

        bytecode.forEach(generatedClassHolder -> {
            try {
                String className = generatedClassHolder.getName();
                File base = new File(compilationUnit.getFilePath());
                File compileFile = new File(base.getParentFile(), className + ".class");
                LOGGER.info("Finished Compiling. Saving bytecode to '{}'.", compileFile.getAbsolutePath());
                OutputStream os = new FileOutputStream(compileFile);
                IOUtils.write(generatedClassHolder.getBytes(), os);
                LOGGER.info("Done. To run compiled file execute: 'java {}' in current dir", className);
            } catch (Exception e) {
                e.printStackTrace();
            }

        });
    }
}
