plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // включва външна зависимост, която дава връзката между обектния и релационния модел. освен тези за тестване на приложения, са включени Hibernate Core, за да мога да ползвам object relation mapping технологията (ORM), за да осъществя контрол върху базата и връзка между Java и базата. според доставчика на базата имам и connector. имам и логове, които са резултатите от изпълнението на заявките.
    implementation("org.hibernate.orm:hibernate-core:7.1.0.Final")
    implementation("com.mysql:mysql-connector-j:9.4.0")
    implementation ("org.apache.logging.log4j:log4j-core:2.17.1")
    implementation("org.hibernate.validator:hibernate-validator:9.0.0.Final")
    implementation("jakarta.validation:jakarta.validation-api:3.1.1")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    // използва се, за да работя по-лесно с моделите. Целта е да използвам анотации и да си спестя Boiler Plate Code – експлицитното писане на конструктори, getter, setter, toString, equals, hash code. Така моделът на данни няма да е толкова обемен.
    compileOnly("org.projectlombok:lombok:1.18.38")
    annotationProcessor("org.projectlombok:lombok:1.18.38")
}

tasks.test {
    useJUnitPlatform()
}