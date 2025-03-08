package com.taskmanager

import org.junit.runner.RunWith
import org.junit.runners.Suite

/**
 * Author: Hari K
 * Date: 08/03/2025.
 */
@RunWith(Suite::class)
@Suite.SuiteClasses(
    TaskCreationFlowTest::class,
    TaskListFilterAndSortTest::class
)
class TaskManagerTestSuite
