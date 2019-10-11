package com.team2813.frc2019.config.motor;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.File;
import java.io.IOException;
import java.util.Map;

@JsonRootName(value = "motors")
public class MotorConfig {

	private Map<String, Motor> motors;

	public Map<String, Motor> getMotors() {
		return motors;
	}

	public void setMotors(Map<String, Motor> motors) {
		this.motors = motors;
	}

	public static void main(String[] args) throws IOException {
		ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

		String fileName = "motorConfig.yaml";
		ClassLoader classLoader = MotorConfig.class.getClassLoader();

		File file = new File(classLoader.getResource(fileName).getFile());
		//File is found
		System.out.println("File Found : " + file.exists());

		//Read File Content
//		String content = new String(Files.readAllBytes(file.toPath()));
//		System.out.println(content);

		MotorConfig order = mapper.readValue(file, MotorConfig.class);
	}

}
