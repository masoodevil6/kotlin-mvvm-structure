package gog.kotlin_db.data.base.Where

import gog.kotlin_db.data.base.QueryBuilder

class QueryBuilder_ModelWhere(
    val whereKey: String,
    var whereValue: String,
    var whereOperation: String = QueryBuilder.Companion._OPERATION_EQUALS,
    var whereLogical: String = QueryBuilder.Companion._LOGICAL_AND
) : IQueryBuilder_ModelWhereNode {

    init {

    }

    override fun toStr(isFirst: Boolean): String {
        val logicPart = if (isFirst) "" else " $whereLogical "
        return "$logicPart$whereKey $whereOperation ($whereValue)"
    }


}