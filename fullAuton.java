package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import java.util.List;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaCurrentGame;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TfodCurrentGame;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous(name = "Autonomus")
public class Autonomus extends LinearOpMode {

  private VuforiaCurrentGame vuforiaUltimateGoal;
  private TfodCurrentGame tfodUltimateGoal;

  Recognition recognition;
  
  private DcMotor RightFront;
  private DcMotor RightBack;
  private DcMotor LeftFront;
  private DcMotor LeftBack;
  private DcMotor leftflywheel;
  private DcMotor rightflywheel;
  private DcMotor intake;
  private String state;
  private double numberOfRings;
  private Servo FrontWobble;
  private Servo BackWobble;

  /**
   * This function is executed when this Op Mode is selected from the Driver Station.
   */
  @Override
  public void runOpMode() {
    List<Recognition> recognitions;
    double index;

    vuforiaUltimateGoal = new VuforiaCurrentGame();
    tfodUltimateGoal = new TfodCurrentGame();

    // Sample TFOD Op Mode
    // Initialize Vuforia.
    // This sample assumes phone is in landscape mode.
    // Rotate phone -90 so back camera faces "forward" direction on robot.
    // We need Vuforia to provide TFOD with camera images.
    vuforiaUltimateGoal.initialize(
        "", // vuforiaLicenseKey
        hardwareMap.get(WebcamName.class, "Webcam 1"), // cameraName
        "", // webcamCalibrationFilename
        false, // useExtendedTracking
        true, // enableCameraMonitoring
        VuforiaLocalizer.Parameters.CameraMonitorFeedback.AXES, // cameraMonitorFeedback
        0, // dx
        0, // dy
        0, // dz
        90, // xAngle
        180, // yAngle
        90, // zAngle
        true); // useCompetitionFieldTargetLocations
    // Set min confidence threshold to 0.7
    tfodUltimateGoal.initialize(vuforiaUltimateGoal, 0.5F, true, true);
    // Initialize TFOD before waitForStart.
    // Init TFOD here so the object detection labels are visible
    // in the Camera Stream preview window on the Driver Station.
    tfodUltimateGoal.activate();
    // Enable following block to zoom in on target.
    tfodUltimateGoal.setZoom(3, 17 / 9);
    telemetry.addData(">", "Press Play to start");
    telemetry.update();
    // Wait for start command from Driver Station.
    
    RightFront = hardwareMap.get(DcMotor.class, "RightFront");
    RightBack = hardwareMap.get(DcMotor.class, "RightBack");
    LeftFront = hardwareMap.get(DcMotor.class, "LeftFront");
    LeftBack = hardwareMap.get(DcMotor.class, "LeftBack");
    leftflywheel = hardwareMap.get(DcMotor.class, "leftflywheel");
    rightflywheel = hardwareMap.get(DcMotor.class, "rightflywheel");
    intake = hardwareMap.get(DcMotor.class, "intake");
    FrontWobble = hardwareMap.get(Servo.class, "FrontWobble");
    BackWobble = hardwareMap.get(Servo.class, "BackWobble");

    RightFront.setDirection(DcMotorSimple.Direction.REVERSE);
    //RightBack.setDirection(DcMotorSimple.Direction.REVERSE);
    //LeftFront.setDirection(DcMotorSimple.Direction.REVERSE);
    LeftBack.setDirection(DcMotorSimple.Direction.REVERSE);
    leftflywheel.setDirection(DcMotorSimple.Direction.REVERSE);
    rightflywheel.setDirection(DcMotorSimple.Direction.REVERSE);
    intake.setDirection(DcMotorSimple.Direction.REVERSE);
    // Put initialization blocks here.
    
    
    waitForStart();
    if (opModeIsActive()) {
      numberOfRings = 0;
      BackWobble.setPosition(0);
      sleep(2000);
      FrontWobble.setPosition(0);
      recognitions = tfodUltimateGoal.getRecognitions();
      // If list is empty, inform the user. Otherwise, go
      // through list and display info for each recognition.
      if (recognitions.size() == 0) {
          telemetry.addData("TFOD", "No items detected.");
          numberOfRings = 0;
      } else {
          index = 0;
          // Iterate through list and call a function to
          // display info for each recognized object.
          for (Recognition recognition_item : recognitions) {
            recognition = recognition_item;
            if (recognition.getLabel() == "Quad") {
              numberOfRings = 4;
              break;
            }
            else if (recognition.getLabel() == "Single") {
              numberOfRings = numberOfRings + 1;
              if (numberOfRings > 1) {
                telemetry.update();
                numberOfRings = 4;
                break;
              }
            }
            // Display info.
            displayInfo(index);
            // Increment index.
            index = index + 1;
          }
      }
      
      sleep(1000);
      
      telemetry.addData("Vuforia Done ", numberOfRings);
      
      telemetry.update();
      
      goStraight(50);
      

      leftflywheel.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
      rightflywheel.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
      ((DcMotorEx) rightflywheel).setVelocity(1010);
      ((DcMotorEx) leftflywheel).setVelocity(1010);
      sleep(1000);
      for (int count = 0; count < 3; count++) {
        if (gamepad1.dpad_down == true) {
          break;
        }
        intake.setPower(0.8);
        sleep(1350);
        intake.setPower(0);
        sleep(500);
      }
      ((DcMotorEx) rightflywheel).setVelocity(0);
      ((DcMotorEx) leftflywheel).setVelocity(0);
      // Put run blocks here.
     
      if (numberOfRings == 0) {
        //do stuff
        goStraight(4000);
        FrontWobble.setPosition(1);
        BackWobble.setPosition(1);
        
        
      }
      else if (numberOfRings == 1) {
        LeftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        LeftBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        LeftFront.setTargetPosition(250);
        LeftBack.setTargetPosition(250);

        LeftBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        LeftFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        ((DcMotorEx) LeftFront).setVelocity(650);
        ((DcMotorEx) LeftBack).setVelocity(650);
        while (LeftFront.isBusy()) {
        }
        ((DcMotorEx) LeftFront).setVelocity(0);
        ((DcMotorEx) LeftBack).setVelocity(0);

        sleep(3000);

        goStraight(2600);

        LeftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        RightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        LeftBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        RightBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        LeftFront.setTargetPosition(-550);
        RightFront.setTargetPosition(550);
        LeftBack.setTargetPosition(-550);
        RightBack.setTargetPosition(550);
        LeftFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        LeftBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        RightBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        RightFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        ((DcMotorEx) LeftFront).setVelocity(-600);
        ((DcMotorEx) LeftBack).setVelocity(-600);
        ((DcMotorEx) RightFront).setVelocity(600);
        ((DcMotorEx) RightBack).setVelocity(600);
        while (LeftFront.isBusy()) {
        }
        ((DcMotorEx) LeftFront).setVelocity(0);
        ((DcMotorEx) LeftBack).setVelocity(0);
        ((DcMotorEx) RightFront).setVelocity(0);
        ((DcMotorEx) RightBack).setVelocity(0);
        LeftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        LeftBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        RightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        RightBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        goStraight(2500);

        FrontWobble.setPosition(1);
        BackWobble.setPosition(1);
        
        goBackwards(-1000);
      }
      else {
        //do stuff finally
        LeftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        LeftBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        LeftFront.setTargetPosition(250);
        LeftBack.setTargetPosition(250);

        LeftBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        LeftFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        ((DcMotorEx) LeftFront).setVelocity(650);
        ((DcMotorEx) LeftBack).setVelocity(650);
        while (LeftFront.isBusy()) {
        }
        ((DcMotorEx) LeftFront).setVelocity(0);
        ((DcMotorEx) LeftBack).setVelocity(0);

        sleep(3000);

        goStraight(2600);

        LeftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        RightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        LeftBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        RightBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        LeftFront.setTargetPosition(-150);
        RightFront.setTargetPosition(150);
        LeftBack.setTargetPosition(-150);
        RightBack.setTargetPosition(150);
        LeftFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        LeftBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        RightBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        RightFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        ((DcMotorEx) LeftFront).setVelocity(-600);
        ((DcMotorEx) LeftBack).setVelocity(-600);
        ((DcMotorEx) RightFront).setVelocity(600);
        ((DcMotorEx) RightBack).setVelocity(600);
        while (LeftFront.isBusy()) {
        }
        ((DcMotorEx) LeftFront).setVelocity(0);
        ((DcMotorEx) LeftBack).setVelocity(0);
        ((DcMotorEx) RightFront).setVelocity(0);
        ((DcMotorEx) RightBack).setVelocity(0);
        LeftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        LeftBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        RightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        RightBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        goStraight(3850);

        FrontWobble.setPosition(1);
        BackWobble.setPosition(1);
        
        goBackwards(-2000);
      }

      telemetry.update();

      
    }
    
    
    
    sleep(100000);
    // Deactivate TFOD.
    tfodUltimateGoal.deactivate();

    vuforiaUltimateGoal.close();
    tfodUltimateGoal.close();
  }

  /**
   * Display info (using telemetry) for a recognized object.
   */
   
  private void goBackwards(int i) {
    LeftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    RightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    LeftBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    RightBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    LeftFront.setTargetPosition(i);
    RightFront.setTargetPosition(i);
    LeftBack.setTargetPosition(i);
    RightBack.setTargetPosition(i);
    LeftFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    LeftBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    RightBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    RightFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    ((DcMotorEx) LeftFront).setVelocity(-1200);
    ((DcMotorEx) LeftBack).setVelocity(-1200);
    ((DcMotorEx) RightFront).setVelocity(-1200);
    ((DcMotorEx) RightBack).setVelocity(-1200);
    while (LeftFront.isBusy()) {
    }
    ((DcMotorEx) LeftFront).setVelocity(0);
    ((DcMotorEx) LeftBack).setVelocity(0);
    ((DcMotorEx) RightFront).setVelocity(0);
    ((DcMotorEx) RightBack).setVelocity(0);
    LeftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    LeftBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    RightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    RightBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
  }
   
  private void goStraight(int i) {
    LeftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    RightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    LeftBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    RightBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    LeftFront.setTargetPosition(i);
    RightFront.setTargetPosition(i);
    LeftBack.setTargetPosition(i);
    RightBack.setTargetPosition(i);
    LeftFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    LeftBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    RightBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    RightFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    ((DcMotorEx) LeftFront).setVelocity(1200);
    ((DcMotorEx) LeftBack).setVelocity(1200);
    ((DcMotorEx) RightFront).setVelocity(1200);
    ((DcMotorEx) RightBack).setVelocity(1200);
    while (LeftFront.isBusy()) {
    }
    ((DcMotorEx) LeftFront).setVelocity(0);
    ((DcMotorEx) LeftBack).setVelocity(0);
    ((DcMotorEx) RightFront).setVelocity(0);
    ((DcMotorEx) RightBack).setVelocity(0);
    LeftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    LeftBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    RightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    RightBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
  }
   
  private void displayInfo(double i) {
    // Display label info.
    // Display the label and index number for the recognition.
    telemetry.addData("label " + i, recognition.getLabel());
    // Display upper corner info.
    // Display the location of the top left corner
    // of the detection boundary for the recognition
    telemetry.addData("Left, Top " + i, recognition.getLeft() + ", " + recognition.getTop());
    // Display lower corner info.
    // Display the location of the bottom right corner
    // of the detection boundary for the recognition
    telemetry.addData("Right, Bottom " + i, recognition.getRight() + ", " + recognition.getBottom());
  }
}