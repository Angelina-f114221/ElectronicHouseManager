plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // включва външни зависимости, които дават връзката между обектния и релационния модел. освен тези за тестване на приложения, са включени Hibernate Core, за да мога да ползвам object relation mapping технологията (ORM), за да осъществя контрол върху базата и връзка между Java и базата. според доставчика на базата имам и connector. имам и логове, които са резултатите от изпълнението на заявките.
    implementation("org.hibernate.orm:hibernate-core:7.1.0.Final")
    implementation("com.mysql:mysql-connector-j:9.4.0")
    implementation("org.apache.logging.log4j:log4j-core:2.17.1")

    // Bean Validation - за валидиране на данни с анотации
    implementation("org.hibernate.validator:hibernate-validator:9.0.0.Final")
    implementation("jakarta.validation:jakarta.validation-api:3.1.1")
    implementation("org.glassfish.expressly:expressly:6.0.0")
    implementation("jakarta.el:jakarta.el-api:6.0.0")

    // използва се, за да работя по-лесно с моделите. Целта е да използвам анотации и да си спестя Boiler Plate Code – експлицитното писане на конструктори, getter, setter, toString, equals, hash code. Така моделът на данни няма да е толкова обемен.
    compileOnly("org.projectlombok:lombok:1.18.38")
    annotationProcessor("org.projectlombok:lombok:1.18.38")
    annotationProcessor("org.hibernate.javax.persistence:hibernate-jpa-2.1-api:1.0.2.Final")

    // JUnit 5 - за Unit тестване. Включва jupiter API, engine за изпълнение, и параметризирани тестове
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.0")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.10.0")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.10.0")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:1.10.0")

    // Mockito - за mockиране на обекти при тестване
    testImplementation("org.mockito:mockito-core:5.7.0")
    testImplementation("org.mockito:mockito-junit-jupiter:5.7.0")

    // AssertJ - за по-красиви и четими assertions в тестовете
    testImplementation("org.assertj:assertj-core:3.24.1")
}

tasks.test {
    useJUnitPlatform()
}
