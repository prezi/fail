# Used by the CI system at Prezi

from prezi.fabric.placement import CommonTasks, PlacementDeploy, PercentageParallelOrchestration

fail_api = CommonTasks(PlacementDeploy(egg_name='fail-api'), 'fail-api', {}, '/', orchestration=PercentageParallelOrchestration(26)).deploy
fail_scheduler = CommonTasks(PlacementDeploy(egg_name='fail-scheduler'), 'fail-scheduler', {}, '/', orchestration=PercentageParallelOrchestration(26)).deploy
