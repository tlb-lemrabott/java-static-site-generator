# Java Static Site Generator

A Spring Boot application that converts JSON site descriptions into static HTML websites.

## What it does
- Takes JSON input → Generates HTML pages using Thymeleaf templates
- Builds deployable static sites ready for hosting

## Tech Stack
- Java 17+
- Spring Boot 3+
- Thymeleaf
- Maven

## Quick Start

1. **Clone & Build**
```bash
git clone https://github.com/yourusername/static-site-generator.git
cd static-site-generator

# Build generator service
cd generator-service && mvn clean package

# Build builder service  
cd ../builder-service && mvn clean package
```

2. **Run Generator Service**
```bash
java -jar generator-service/target/generator-service.jar
```
Upload your JSON file via `POST /api/generate` at `http://localhost:8080`

3. **Run Builder Service**
```bash
java -jar builder-service/target/builder-service.jar
```
Build your site via `GET /api/build?siteName=YourSiteName` at `http://localhost:8081`

## JSON Input Example
```json
{
  "siteName": "MySite",
  "pages": [
    {
      "title": "Home",
      "slug": "index",
      "sections": [
        {"type": "hero", "heading": "Welcome!", "text": "Hello World"}
      ]
    }
  ]
}
```

## Output
- Generator creates: `/output/MySite/` with HTML files
- Builder creates: `/build/MySite/` ready for deployment

Deploy `/build/MySite/` to any static host (GitHub Pages, Netlify, S3).

---
**Author:** Lemrabott Toulba | **License:** MIT