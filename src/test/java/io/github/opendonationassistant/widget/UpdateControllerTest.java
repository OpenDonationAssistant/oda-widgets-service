package io.github.opendonationassistant.widget;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class UpdateControllerTest {

  @Test
  public void testMigratingDefaultGoalToDefaultMode() {
    var goals = List.<Map<String, Object>>of(
      Map.<String, Object>of("id", "goal-1", "default", true)
    );
    var migrated = UpdateController.migrateGoals(goals);
    assertEquals("default", migrated.get(0).get("mode"));
    assertFalse(migrated.get(0).containsKey("default"));
  }

  @Test
  public void testMigratingNonDefaultGoalToChooseMode() {
    var goals = List.<Map<String, Object>>of(
      Map.<String, Object>of("id", "goal-1", "default", false)
    );
    var migrated = UpdateController.migrateGoals(goals);
    assertEquals("choose", migrated.get(0).get("mode"));
    assertFalse(migrated.get(0).containsKey("default"));
  }

  @Test
  public void testAlreadyMigratedGoalsAreUntouched() {
    var goals = List.<Map<String, Object>>of(
      Map.<String, Object>of("id", "goal-1", "mode", "default")
    );
    var migrated = UpdateController.migrateGoals(goals);
    assertEquals("default", migrated.get(0).get("mode"));
  }
}