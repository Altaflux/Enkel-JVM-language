package com.kubadziworski.bytecodegeneration.statement;

import com.kubadziworski.domain.node.expression.Expression;
import com.kubadziworski.domain.node.statement.Assignment;
import com.kubadziworski.domain.scope.Field;
import com.kubadziworski.domain.scope.LocalVariable;
import com.kubadziworski.domain.scope.Scope;
import com.kubadziworski.domain.type.Type;
import com.kubadziworski.exception.FinalFieldModificationException;
import com.kubadziworski.exception.IncompatibleTypesException;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.Optional;

public class AssignmentStatementGenerator {
    private final MethodVisitor methodVisitor;


    public AssignmentStatementGenerator(MethodVisitor methodVisitor) {
        this.methodVisitor = methodVisitor;
    }

    public void generate(Assignment assignment, Scope scope, StatementGenerator generator) {

        String varName = assignment.getVarName();
        Expression expression = assignment.getAssignmentExpression();
        Type type = expression.getType();


        Field field;
        String descriptor;
        Optional<Expression> preExpression = assignment.getPreExpression();
        if (preExpression.isPresent()) {

            //TODO USE SCOPE
            Expression exp = preExpression.get();
            if (exp.getType().equals(scope.getClassType())) {
                field = scope.getField(varName);
            } else {
                field = exp.getType().getField(varName);
            }

            descriptor = field.getType().getDescriptor();
            preExpression.get().accept(generator);
        } else {

            if (scope.isLocalVariableExists(varName)) {
                int index = scope.getLocalVariableIndex(varName);
                LocalVariable localVariable = scope.getLocalVariable(varName);
                if (!localVariable.isMutable()) {
                    if (!assignment.isVariableDeclaration()) {
                        throw new FinalFieldModificationException("Cannot modify final variable: " + localVariable.getName());
                    }
                }
                Type localVariableType = localVariable.getType();
                if (expression.getType().inheritsFrom(localVariableType) < 0) {
                    throw new IncompatibleTypesException(varName, localVariableType, expression.getType());
                }
                expression.accept(generator);
                castIfNecessary(type, localVariableType);
                methodVisitor.visitVarInsn(type.getStoreVariableOpcode(), index);
                return;
            }

            field = scope.getField(varName);
            descriptor = field.getType().getDescriptor();
            methodVisitor.visitVarInsn(Opcodes.ALOAD, 0);
        }

        expression.accept(generator);
        castIfNecessary(type, field.getType());
        methodVisitor.visitFieldInsn(Opcodes.PUTFIELD, field.getOwnerInternalName(), field.getName(), descriptor);

    }

    private void castIfNecessary(Type expressionType, Type variableType) {
        if (!expressionType.equals(variableType)) {
            methodVisitor.visitTypeInsn(Opcodes.CHECKCAST, variableType.getInternalName());
        }
    }
}