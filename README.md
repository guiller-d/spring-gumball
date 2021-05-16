# CMPE 172 - Lab #10 - DevOps CI/CD

### CI Workflow (Part 1)



![alt text](https://github.com/guiller-d/spring-gumball/blob/master/screenshots/image1.png)

**Create Java CI with Gradle yml file (gradle.yml)**

![alt text](https://github.com/guiller-d/spring-gumball/blob/master/screenshots/image2.png)

**Create Build and Deploy to GKE yml file (google.yml)**

![alt text](https://github.com/guiller-d/spring-gumball/blob/master/screenshots/image3.png)

**Check if building gradle.yml is successful.**

![alt text](https://github.com/guiller-d/spring-gumball/blob/master/screenshots/image2.2.png)

### CD Workflow (Part 2)

**Create kustomization.yml file**

![alt text](https://github.com/guiller-d/spring-gumball/blob/master/screenshots/image4.png)

**Check workflow builds to check if CI is working. It's working**

![alt text](https://github.com/guiller-d/spring-gumball/blob/master/screenshots/image5.png)

**Updated deployment.yaml to the one show below**
``` 
apiVersion: apps/v1
kind: Deployment
metadata:
  name: spring-gumball-deployment
  namespace: default
spec:
  selector:
    matchLabels:
      name: spring-gumball
  replicas: 2 # tells deployment to run 2 pods matching the template
  template: # create pods using pod definition in this template
    metadata:
      # unlike pod.yaml, the name is not included in the meta data as a unique name is
      # generated from the deployment name
      labels:
        name: spring-gumball
    spec:
      containers:
      - name: spring-gumball
        image: gcr.io/PROJECT_ID/IMAGE:TAG
        ports:
        - containerPort: 8080
```
**Create GCP Service Accoucnt & JSON Service Account Key**

![alt text](https://github.com/guiller-d/spring-gumball/blob/master/screenshots/image6.png)

**Update the GKE_PROJECT value to my project name which is grand-highway-304300 and GKE_SA_KEY (generated JSON file)**

![alt text](https://github.com/guiller-d/spring-gumball/blob/master/screenshots/image7.png)

**Check if building google.yml is successful.** 

![alt text](https://github.com/guiller-d/spring-gumball/blob/master/screenshots/image9.png)

**Check workflow builds.**

![alt text](https://github.com/guiller-d/spring-gumball/blob/master/screenshots/image10.png)

**Create Load Balancer**

![alt text](https://github.com/guiller-d/spring-gumball/blob/master/screenshots/image11.png)

**Goto dynamically created IP by the Load Balancer (see red line)**

![alt text](https://github.com/guiller-d/spring-gumball/blob/master/screenshots/image13.png)

**Spring Gumball V2 is displayed**

![alt text](https://github.com/guiller-d/spring-gumball/blob/master/screenshots/image14.png)

