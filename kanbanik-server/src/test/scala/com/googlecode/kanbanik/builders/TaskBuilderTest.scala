package com.googlecode.kanbanik.builders
import org.bson.types.ObjectId
import org.junit.runner.RunWith
import org.mockito.Mockito.when
import org.scalatest.junit.JUnitRunner
import org.scalatest.mock.MockitoSugar
import org.scalatest.Spec
import com.googlecode.kanbanik.model.TaskScala
import com.googlecode.kanbanik.model.WorkflowitemScala
import com.googlecode.kanbanik.dto.ClassOfService
import com.googlecode.kanbanik.model.ProjectScala
import com.googlecode.kanbanik.dto.ProjectDto

@RunWith(classOf[JUnitRunner])
class TaskBuilderTest extends Spec with MockitoSugar {
  describe("TaskBuilder should be able to build DTOs from entity objects and vice versa") {

    it("should be able to fill all properties") {
      val task = mock[TaskScala]
      val workflowitem = mock[WorkflowitemScala]
      
      when(workflowitem.id).thenReturn(Some(new ObjectId("6f48e10644ae3742baa2d0a9")))
      when(workflowitem.child).thenReturn(None)
      when(workflowitem.itemType).thenReturn("H")

      when(task.id).thenReturn(Some(new ObjectId("4f48e10644ae3742baa2d0a9")))
      when(task.name).thenReturn("someName")
      when(task.description).thenReturn("someDesc")
      when(task.classOfService).thenReturn(2)
      when(task.workflowitem).thenReturn(workflowitem)

      val taskBuilder = new TestedTaskBuilder
      val res = taskBuilder.buildDto(task)

      assert(res.getId() === "4f48e10644ae3742baa2d0a9")
      assert(res.getName() === "someName")
      assert(res.getDescription() === "someDesc")
      assert(res.getClassOfService() === ClassOfService.fromId(2))
      assert((res.getWorkflowitem() != null) === true)
    }
  }
  
  class TestedTaskBuilder extends TaskBuilder {
    
    override def findProjectForTask(task: TaskScala) = Some(mock[ProjectScala])
    
    override def projectBuilder = new SimpleProjectBuilder
    
    class SimpleProjectBuilder extends ProjectBuilder {
      override def buildShallowDto(project: ProjectScala) = new ProjectDto
    }
  }
}