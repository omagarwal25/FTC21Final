package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp(name = "FullDriveJava")
public class FullDriveJava extends LinearOpMode {

  private DcMotor LeftFront;
  private DcMotor LeftBack;
  private DcMotor RightFront;
  private DcMotor leftflywheel;
  private DcMotor rightflywheel;
  private DcMotor RightBack;
  private DcMotor intake;
  private float LeftStickY;
  private Servo FrontWobble;
  private Servo BackWobble;

  /**
   * This function is executed when this Op Mode is selected from the Driver Station.
   */
  @Override
  public void runOpMode() {
    LeftFront = hardwareMap.get(DcMotor.class, "LeftFront");
    LeftBack = hardwareMap.get(DcMotor.class, "LeftBack");
    RightFront = hardwareMap.get(DcMotor.class, "RightFront");
    leftflywheel = hardwareMap.get(DcMotor.class, "leftflywheel");
    rightflywheel = hardwareMap.get(DcMotor.class, "rightflywheel");
    RightBack = hardwareMap.get(DcMotor.class, "RightBack");
    intake = hardwareMap.get(DcMotor.class, "intake");
    FrontWobble = hardwareMap.get(Servo.class, "FrontWobble");
    BackWobble = hardwareMap.get(Servo.class, "BackWobble");


    // Put initialization blocks here.
    LeftFront.setDirection(DcMotorSimple.Direction.REVERSE);
    //LeftBack.setDirection(DcMotorSimple.Direction.REVERSE);
    RightBack.setDirection(DcMotorSimple.Direction.REVERSE);
    leftflywheel.setDirection(DcMotorSimple.Direction.REVERSE);
    rightflywheel.setDirection(DcMotorSimple.Direction.REVERSE);
    waitForStart();
    if (opModeIsActive()) {
      // Put run blocks here.
      while (opModeIsActive()) {
        
        if (0.1 > gamepad1.left_stick_y && gamepad1.left_stick_y > -0.1) {
          LeftStickY = 0;
        }
        else {
          LeftStickY = gamepad1.left_stick_y;
        }
        
        
        if (gamepad1.left_trigger == 0) {
            LeftFront.setPower(((LeftStickY - (gamepad1.left_stick_x / 3)) - gamepad1.right_stick_x));
            RightFront.setPower(((LeftStickY + (gamepad1.left_stick_x / 3)) + gamepad1.right_stick_x));
            LeftBack.setPower(((LeftStickY + (gamepad1.left_stick_x / 2.5)) - gamepad1.right_stick_x));
            RightBack.setPower(((LeftStickY - (gamepad1.left_stick_x / 2.5 )) + gamepad1.right_stick_x));
        }
        else {
            LeftFront.setPower(((LeftStickY - (gamepad1.left_stick_x / 2)) - gamepad1.right_stick_x) / 1.5);
            RightFront.setPower(((LeftStickY + (gamepad1.left_stick_x / 2)) + gamepad1.right_stick_x) / 1.5);
            LeftBack.setPower(((LeftStickY + (gamepad1.left_stick_x / 1.5)) - gamepad1.right_stick_x) / 1.5);
            RightBack.setPower(((LeftStickY - (gamepad1.left_stick_x / 1.5)) + gamepad1.right_stick_x) / 1.5);
        }
        
        if (gamepad2.right_bumper == true) {
          intake.setPower(0.8);
        } else if (gamepad2.left_bumper == true) {
          intake.setPower(-0.8);
        } else {
          intake.setPower(0);
        }
        if (gamepad2.triangle == true) {
          ((DcMotorEx) leftflywheel).setVelocity(1025);
          ((DcMotorEx) rightflywheel).setVelocity(1025);
        }
        if (gamepad2.circle == true) {
          ((DcMotorEx) leftflywheel).setVelocity(850);
          ((DcMotorEx) rightflywheel).setVelocity(850);
        }
        if (gamepad2.dpad_left == true) {
          BackWobble.setPosition(1);
          FrontWobble.setPosition(1);
        }
        if (gamepad2.dpad_right) {
          BackWobble.setPosition(0);
          FrontWobble.setPosition(0);
        }
        if (gamepad2.cross == true) {
          ((DcMotorEx) leftflywheel).setVelocity(0);
          ((DcMotorEx) rightflywheel).setVelocity(0);
        }
        if (gamepad2.dpad_up == true) {
          ((DcMotorEx) rightflywheel).setVelocity(850);
          ((DcMotorEx) leftflywheel).setVelocity(850);
          sleep(1000);
          for (int count = 0; count < 3; count++) {
            if (gamepad1.dpad_down == true) {
              break;
            }
            intake.setPower(-0.8);
            sleep(1250);
            intake.setPower(0);
            sleep(800);
          }
          ((DcMotorEx) leftflywheel).setVelocity(0);
          ((DcMotorEx) rightflywheel).setVelocity(0);
        }
        telemetry.update();
      }
    }
  }
}