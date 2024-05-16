package com.alibaba.fastjson2.issues_2500;

import com.alibaba.fastjson2.JSON;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Issue2548 {
    @Test
    public void fastJson() {
        CarDTO carDTO = new CarDTO();
        carDTO.setAge(10);

        String jsonString = JSON.toJSONString(carDTO);
        assertEquals("{\"vehicle_type\":\"Car\",\"age\":10}", jsonString);

        VehicleDTO vehicleDTO = JSON.parseObject(jsonString, VehicleDTO.class);
        assertEquals(vehicleDTO.getClass(), CarDTO.class);
        assertEquals("Car", vehicleDTO.getVehicleType());
    }

    @Test
    public void jackson() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        CarDTO carDTO = new CarDTO();
        carDTO.setAge(10);
        String jsonString = objectMapper.writeValueAsString(carDTO);
        VehicleDTO vehicleDTO = objectMapper.readValue(jsonString, VehicleDTO.class);
        assertEquals(vehicleDTO.getClass(), CarDTO.class);
        assertEquals("Car", vehicleDTO.getVehicleType());
    }

    @JsonIgnoreProperties(
            value = "vehicle_type", // ignore manually set animal_type, it will be automatically generated by Jackson during serialization
            allowSetters = true // allows the animal_type to be set during deserialization
    )
    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "vehicle_type", visible = true)
    @JsonSubTypes({
            @JsonSubTypes.Type(value = CarDTO.class, name = "Car"),
            @JsonSubTypes.Type(value = CycleDTO.class, name = "Cycle"),
    })
    public static class VehicleDTO
            implements Serializable {
        private static final long serialVersionUID = 1L;

        private String vehicleType;

        public VehicleDTO() {
            super();
        }

        /**
         * Constructor with only required parameters
         */
        public VehicleDTO(String vehicleType) {
            this.vehicleType = vehicleType;
        }

        public VehicleDTO vehicleType(String vehicleType) {
            this.vehicleType = vehicleType;
            return this;
        }

        /**
         * Get animalType
         *
         * @return animalType
         */
        @JsonProperty("vehicle_type")
        public String getVehicleType() {
            return vehicleType;
        }

        public void setVehicleType(String vehicleType) {
            this.vehicleType = vehicleType;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            VehicleDTO vehicle = (VehicleDTO) o;
            return Objects.equals(this.vehicleType, vehicle.vehicleType);
        }

        @Override
        public int hashCode() {
            return Objects.hash(vehicleType);
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("class VehicleDTO {\n");
            sb.append("    vehicleType: ").append(toIndentedString(vehicleType)).append("\n");
            sb.append("}");
            return sb.toString();
        }

        /**
         * Convert the given object to string with each line indented by 4 spaces
         * (except the first line).
         */
        private String toIndentedString(Object o) {
            if (o == null) {
                return "null";
            }
            return o.toString().replace("\n", "\n    ");
        }
    }

    @JsonTypeName("Car")
    public static class CarDTO
            extends VehicleDTO implements
            Serializable {
        private Integer age;

        public CarDTO() {
            super();
        }

        /**
         * Constructor with only required parameters
         */
        public CarDTO(String vehicleType) {
            super(vehicleType);
        }

        public CarDTO age(Integer age) {
            this.age = age;
            return this;
        }

        /**
         * Get age
         *
         * @return age
         */

        @JsonProperty("age")
        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }

        public CarDTO vehicleType(String vehicleType) {
            super.vehicleType(vehicleType);
            return this;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            CarDTO car = (CarDTO) o;
            return Objects.equals(this.age, car.age) &&
                    super.equals(o);
        }

        @Override
        public int hashCode() {
            return Objects.hash(age, super.hashCode());
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("class CaDTO {\n");
            sb.append("    ").append(toIndentedString(super.toString())).append("\n");
            sb.append("    age: ").append(toIndentedString(age)).append("\n");
            sb.append("}");
            return sb.toString();
        }

        /**
         * Convert the given object to string with each line indented by 4 spaces
         * (except the first line).
         */
        private String toIndentedString(Object o) {
            if (o == null) {
                return "null";
            }
            return o.toString().replace("\n", "\n    ");
        }
    }

    @JsonTypeName("Cycle")
    public static class CycleDTO
            extends VehicleDTO
            implements Serializable {
        public int id;
    }
}