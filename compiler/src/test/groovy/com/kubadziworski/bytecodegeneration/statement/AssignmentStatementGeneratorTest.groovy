package com.kubadziworski.bytecodegeneration.statement

import com.kubadziworski.domain.node.expression.Value
import com.kubadziworski.domain.node.statement.Assignment
import com.kubadziworski.domain.scope.Field
import com.kubadziworski.domain.scope.LocalVariable
import com.kubadziworski.domain.scope.Scope
import com.kubadziworski.domain.type.DefaultTypes
import com.kubadziworski.domain.type.intrinsic.primitive.PrimitiveTypes
import com.kubadziworski.test.DumbType
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.commons.InstructionAdapter
import spock.lang.Specification

import java.lang.reflect.Modifier
/**
 * Created by kuba on 13.05.16.
 */
class AssignmentStatementGeneratorTest extends Specification {

    def "should generate bytecode for local variable if variable for name exists in scope"() {
        given:
            def assignment = new Assignment(varName,assignmentExpression)
            def localVariable = Mock(LocalVariable)
            MethodVisitor methodVisitor = Mock()
            StatementGenerator expressionGenerator = Mock()
            Scope scope = Mock()
        when:
            new AssignmentStatementGenerator(new InstructionAdapter(methodVisitor)).generate(assignment, scope, expressionGenerator)
        then :
            1*scope.isLocalVariableExists(varName) >> true
            1*scope.getLocalVariableIndex(varName) >> 3
            1* scope.getLocalVariable(varName) >> localVariable
            1* localVariable.isMutable() >> true
            1* localVariable.getType() >> assignmentExpression.getType()
            1*methodVisitor.visitVarInsn(expectedOpcode,3)
        where:
            varName  | assignmentExpression                      | expectedOpcode
            "var"    | new Value(PrimitiveTypes.INT_TYPE, "25")  | Opcodes.ISTORE
            "strVar" | new Value(DefaultTypes.STRING, "somestr") | Opcodes.ASTORE
    }

    def "should generate bytecode for assignment if field for name exists in scope but local variable does not"() {
        given:
            def assignment = new Assignment(varName,assignmentExpression)
            def field = new Field(varName, variableOwner, variableType,  Modifier.PUBLIC)
            MethodVisitor methodVisitor = Mock()
            StatementGenerator expressionGenerator = Mock()
            Scope scope = Mock()
        when:
            new AssignmentStatementGenerator(new InstructionAdapter(methodVisitor)).generate(assignment, scope, expressionGenerator)
            then :
            1*scope.isLocalVariableExists(varName) >> false
            1*scope.getField(varName) >> field
            1* methodVisitor.visitFieldInsn(Opcodes.PUTFIELD,field.ownerInternalName,field.name,field.type.descriptor)
        where:
        varName     | variableOwner        | variableType            | assignmentExpression
        "var"       | new DumbType("Main") | PrimitiveTypes.INT_TYPE | new Value(PrimitiveTypes.INT_TYPE, "25")
        "stringVar" | new DumbType("Main") | DefaultTypes.STRING     | new Value(DefaultTypes.STRING, "someString")
    }
}
