package com.kubadziworski.domain.node.expression.prefix;

import com.kubadziworski.bytecodegeneration.statement.StatementGenerator;
import com.kubadziworski.domain.UnaryOperator;
import com.kubadziworski.domain.node.ElementImpl;
import com.kubadziworski.domain.node.NodeData;
import com.kubadziworski.domain.node.expression.Expression;
import com.kubadziworski.domain.node.expression.Reference;
import com.kubadziworski.domain.type.Type;


public class IncrementDecrementExpression extends ElementImpl implements Expression {

    private final Reference reference;
    private final boolean prefix;
    private final UnaryOperator operator;

    public IncrementDecrementExpression(Reference reference, boolean prefix, UnaryOperator operator) {
        this(null, reference, prefix, operator);
    }

    public IncrementDecrementExpression(NodeData nodeData, Reference reference, boolean prefix, UnaryOperator operator) {
        super(nodeData);
        this.reference = reference;
        this.prefix = prefix;
        this.operator = operator;
    }

    public Reference getReference() {
        return reference;
    }

    public boolean isPrefix() {
        return prefix;
    }

    public UnaryOperator getOperator() {
        return operator;
    }

    @Override
    public Type getType() {
        return reference.getType();
    }


    @Override
    public void accept(StatementGenerator generator) {
        generator.generate(this);
    }
}
