package com.kubadziworski.parsing.visitor.statement;

import com.kubadziworski.antlr.EnkelParser;
import com.kubadziworski.antlr.EnkelParser.BlockContext;
import com.kubadziworski.antlr.EnkelParserBaseVisitor;
import com.kubadziworski.domain.node.RuleContextElementImpl;
import com.kubadziworski.domain.node.statement.Block;
import com.kubadziworski.domain.node.statement.Statement;
import com.kubadziworski.domain.scope.FunctionScope;
import com.kubadziworski.exception.UnreachableStatementException;

import java.util.ArrayList;
import java.util.List;

public class BlockStatementVisitor extends EnkelParserBaseVisitor<Block> {
    private final FunctionScope scope;

    public BlockStatementVisitor(FunctionScope scope) {
        this.scope = scope;
    }

    @Override
    public Block visitBlock(BlockContext ctx) {
        List<EnkelParser.StatementContext> blockStatementsCtx = ctx.blockStatement().statement();
        FunctionScope newScope = new FunctionScope(scope);
        StatementVisitor statementVisitor = new StatementVisitor(newScope);

        List<Statement> statements = new ArrayList<>();
        boolean hasReturnCompleted = false;
        for (EnkelParser.StatementContext statementContext : blockStatementsCtx) {
            if (hasReturnCompleted) {
                throw new UnreachableStatementException(statementContext);
            }

            Statement statement = statementContext.accept(statementVisitor);
            statements.add(statement);
            hasReturnCompleted = statement.isReturnComplete();
        }
        return new Block(new RuleContextElementImpl(ctx), newScope, statements);
    }
}