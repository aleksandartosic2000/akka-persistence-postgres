/*
 * Copyright (C) 2014 - 2019 Dennis Vriend <https://github.com/dnvriend>
 * Copyright (C) 2019 - 2020 Lightbend Inc. <https://www.lightbend.com>
 */

package akka.persistence.postgres.journal.dao

import akka.persistence.postgres.TablesTestSpec

class JournalTablesTest extends TablesTestSpec {
  val journalTableConfiguration = journalConfig.journalTableConfiguration

  for {
    (journalName, journalTable) <- List(
      ("FlatJournalTable", FlatJournalTable(journalTableConfiguration)),
      ("PartitionedJournalTable", PartitionedJournalTable(journalTableConfiguration)),
      ("NestedPartitionsJournalTable", NestedPartitionsJournalTable(journalTableConfiguration)))
  } {
    journalName should "be configured with a schema name" in {
      journalTable.baseTableRow.schemaName shouldBe journalTableConfiguration.schemaName
    }

    it should "be configured with a table name" in {
      journalTable.baseTableRow.tableName shouldBe journalTableConfiguration.tableName
    }

    it should "be configured with column names" in {
      val colName = toColumnName(journalTableConfiguration.tableName)(_)
      journalTable.baseTableRow.persistenceId.toString shouldBe colName(
        journalTableConfiguration.columnNames.persistenceId)
      journalTable.baseTableRow.deleted.toString shouldBe colName(journalTableConfiguration.columnNames.deleted)
      journalTable.baseTableRow.sequenceNumber.toString shouldBe colName(
        journalTableConfiguration.columnNames.sequenceNumber)
      journalTable.baseTableRow.tags.toString shouldBe colName(journalTableConfiguration.columnNames.tags)
    }
  }
}
