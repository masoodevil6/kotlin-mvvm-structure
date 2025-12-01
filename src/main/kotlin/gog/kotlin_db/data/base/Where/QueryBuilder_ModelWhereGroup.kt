package gog.kotlin_db.data.base.Where

import gog.kotlin_db.data.base.QueryBuilder
/*
class QueryBuilder_ModelWhereGroup(
    var whereLogical: String = QueryBuilder._LOGICAL_AND,
    val group: List<IQueryBuilder_ModelWhereNode>
) : IQueryBuilder_ModelWhereNode {

    override fun toStr(isFirst: Boolean): String {
        val inside = group.mapIndexed { index, child ->
            child.toStr(index == 0)
        }.joinToString(" ")

        val prefix = if (isFirst) "" else " $whereLogical "
        return "$prefix($inside)"
    }

}*/
class QueryBuilder_ModelWhereGroup(
    var whereLogical: String = QueryBuilder._LOGICAL_AND,
    val query: String
) : IQueryBuilder_ModelWhereNode {

    override fun toStr(isFirst: Boolean): String {
        val prefix = if (isFirst) "" else " $whereLogical "
        return "$prefix($query)"
    }

}