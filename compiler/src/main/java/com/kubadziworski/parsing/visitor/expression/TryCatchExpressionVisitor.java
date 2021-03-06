package com.kubadziworski.parsing.visitor.expression;

import com.kubadziworski.antlr.EnkelParser;
import com.kubadziworski.antlr.EnkelParserBaseVisitor;
import com.kubadziworski.domain.node.RuleContextElementImpl;
import com.kubadziworski.domain.node.expression.BlockExpression;
import com.kubadziworski.domain.node.expression.Parameter;
import com.kubadziworski.domain.node.expression.trycatch.CatchBlock;
import com.kubadziworski.domain.node.expression.trycatch.TryCatchExpression;
import com.kubadziworski.domain.node.statement.Block;
import com.kubadziworski.domain.node.statement.Statement;
import com.kubadziworski.domain.node.statement.TryCatchStatement;
import com.kubadziworski.domain.scope.FunctionScope;
import com.kubadziworski.domain.scope.LocalVariable;
import com.kubadziworski.domain.type.Type;
import com.kubadziworski.parsing.visitor.statement.BlockStatementVisitor;
import com.kubadziworski.util.TypeResolver;

import java.util.List;
import java.util.stream.Collectors;


public class TryCatchExpressionVisitor extends EnkelParserBaseVisitor<Statement> {

    private final ExpressionVisitor expressionVisitor;
    private final FunctionScope scope;

    public TryCatchExpressionVisitor(ExpressionVisitor expressionVisitor, FunctionScope scope) {
        this.expressionVisitor = expressionVisitor;
        this.scope = scope;
    }

    public Statement visitTryExpression(EnkelParser.TryExpressionContext ctx) {

        BlockExpression block = (BlockExpression) ctx.block().accept(expressionVisitor);

        Block finallyBlock = null;
        if (!ctx.finallyBlock().isEmpty()) {
            finallyBlock = ((BlockExpression) ctx.finallyBlock().get(0)
                    .accept(expressionVisitor))
                    .getStatementBlock();
        }
        List<CatchBlock> catchBlocks = ctx.catchBlock().stream()
                .map(this::processCatchBlock).collect(Collectors.toList());

        if (catchBlocks.isEmpty() && finallyBlock == null) {
            throw new RuntimeException("Catch or finally needs to be declared");
        }

        if (catchBlocks.isEmpty()) {
            return new TryCatchStatement(new RuleContextElementImpl(ctx), block.getStatementBlock(), finallyBlock);
        }

        return new TryCatchExpression(new RuleContextElementImpl(ctx), block, catchBlocks, finallyBlock);
    }

    private CatchBlock processCatchBlock(EnkelParser.CatchBlockContext context) {

        FunctionScope functionScope = new FunctionScope(scope);
        String varName = context.name().getText();
        Type varType = TypeResolver.getFromTypeContext(context.type(), scope);

        Parameter parameter = new Parameter(varName, varType, null);
        functionScope.addLocalVariable(new LocalVariable(varName, varType, false));

        BlockStatementVisitor statementGenerator = new BlockStatementVisitor(functionScope);
        Block block = context.block().accept(statementGenerator);

        return new CatchBlock(new BlockExpression(new RuleContextElementImpl(context), block), parameter);
    }
}
