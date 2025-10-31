# Java Static Site Generator

A professional-grade Java/SpringBoot application that generates static websites from JSON descriptions using Spring Boot, Thymeleaf, and enterprise design patterns.

## 🚀 Features

- **JSON-Driven**: Define your entire website structure in a simple JSON file
- **Template Engine**: Uses Thymeleaf for flexible HTML generation
- **Multiple Section Types**: Support for hero, skills, forms, contact, and more
- **Optimization**: Automatic CSS/JS minification and optimization
- **Multi-Platform Deployment**: Generates configs for GitHub Pages, Netlify, Vercel, Apache
- **RESTful API**: Clean REST endpoints for integration
- **Enterprise Architecture**: Follows SOLID principles, separation of concerns, and design patterns

## 🏗️ Architecture

| Module | Description | Port |
|---------|--------------|------|
| 🧩 **site-generator** | Generates static HTML pages from JSON input | 8080 |
| 🌐 **site-builder** | Packages and optimizes sites for deployment | 8081 |

## 🛠️ Tech Stack

- **Java 17+**
- **Spring Boot 3+**
- **Thymeleaf** (Template Engine)
- **Jackson** (JSON Processing)
- **Maven** (Build Tool)
- **Jakarta Validation** (Input Validation)

## 📋 Prerequisites

- Java 17 or higher
- Maven 3.6+
- Git

## 🚀 Quick Start

### 1. Clone and Build

```bash
git clone <repository-url>
cd static-site-generator-app

# Build both services
cd site-generator && mvn clean package
cd ../site-builder && mvn clean package
```

### 2. Run Services

**Terminal 1 - Site Generator:**
```bash
cd site-generator
java -jar target/site-generator-0.0.1-SNAPSHOT.jar
```

**Terminal 2 - Site Builder:**
```bash
cd site-builder
java -jar target/site-builder-0.0.1-SNAPSHOT.jar
```

### 3. Generate Your First Site

**Option A: Using JSON in Request Body**
```bash
curl -X POST http://localhost:8080/api/generate/json \
  -H "Content-Type: application/json" \
  -d @sample-site.json
```

**Option B: Using File Upload**
```bash
curl -X POST http://localhost:8080/api/generate \
  -F "file=@sample-site.json"
```

### 4. Build for Deployment

```bash
curl "http://localhost:8081/api/build?siteName=MyPortfolio"
```

## 📝 JSON Schema

### Site Structure
```json
{
  "siteName": "string (required, 1-100 chars)",
  "pages": [
    {
      "title": "string (required, 1-200 chars)",
      "slug": "string (required, lowercase, numbers, hyphens only)",
      "sections": [
        {
          "type": "string (required, see supported types below)",
          "heading": "string (optional)",
          "text": "string (optional)",
          "items": ["array of strings (optional)"],
          "fields": ["array of strings (optional)"],
          "content": {"key": "value object (optional)"}
        }
      ]
    }
  ]
}
```

### Supported Section Types

| Type | Description | Required Fields | Optional Fields |
|------|-------------|----------------|-----------------|
| `hero` | Hero banner section | - | `heading`, `text` |
| `skills` | Skills list | - | `items` |
| `form` | Contact form | - | `fields` |
| `text` | Text content | - | `heading`, `text` |
| `contact` | Contact information | - | `content` |
| `about` | About section | - | `text` |
| `image` | Image display | - | `heading`, `content` |

### Example JSON
```json
{
  "siteName": "MyPortfolio",
  "pages": [
    {
      "title": "Home",
      "slug": "index",
      "sections": [
        {
          "type": "hero",
          "heading": "Welcome to My Portfolio!",
          "text": "I'm a passionate software developer."
        },
        {
          "type": "skills",
          "items": ["Java", "Spring Boot", "Thymeleaf", "Maven"]
        }
      ]
    },
    {
      "title": "Contact",
      "slug": "contact",
      "sections": [
        {
          "type": "form",
          "fields": ["name", "email", "message"]
        }
      ]
    }
  ]
}
```

## 🔌 API Endpoints

### Site Generator (Port 8080)

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/generate` | Generate site from uploaded JSON file |
| `POST` | `/api/generate/json` | Generate site from JSON in request body |
| `GET` | `/api/health` | Health check |
| `GET` | `/api/section-types` | Get supported section types |

### Site Builder (Port 8081)

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/build` | Build site for deployment |
| `GET` | `/api/sites` | List available sites |
| `GET` | `/api/status/{siteName}` | Get build status |
| `GET` | `/api/health` | Health check |
| `GET` | `/api/deployment-info` | Get deployment options |

## 📁 Output Structure

### Generated Site (`/output/SiteName/`)
```
MyPortfolio/
├── index.html
├── contact.html
├── assets/
│   ├── styles.css
│   └── script.js
└── config.json
```

### Built Site (`/build/SiteName/`)
```
MyPortfolio/
├── index.html
├── contact.html
├── assets/
│   ├── styles.css (minified)
│   └── script.js (minified)
├── .htaccess (Apache config)
├── netlify.toml (Netlify config)
└── README.md (Deployment instructions)
```

## 🚀 Deployment Options

### GitHub Pages
1. Push built site to GitHub repository
2. Enable GitHub Pages in repository settings
3. Select source as "Deploy from a branch"

### Netlify
1. Drag and drop build directory to Netlify
2. Or connect your GitHub repository
3. Set build command to empty (already built)

### Vercel
1. Install Vercel CLI: `npm i -g vercel`
2. Run `vercel` in build directory
3. Follow the prompts

### Apache/Nginx
1. Upload all files to your web server
2. Configure web server to serve static files
3. Use included `.htaccess` file for Apache

## 🧪 Testing

Run the test suite:
```bash
# Site Generator Tests
cd site-generator && mvn test

# Site Builder Tests
cd site-builder && mvn test
```

## 🏗️ Architecture Patterns

This project follows enterprise-grade design patterns:

- **Domain-Driven Design (DDD)**: Clear domain models and boundaries
- **Service Layer Pattern**: Business logic separation
- **Repository Pattern**: Data access abstraction
- **DTO Pattern**: Data transfer objects for API communication
- **Factory Pattern**: Template generation
- **Strategy Pattern**: Different section type handling
- **SOLID Principles**: Single responsibility, open/closed, etc.

## 🔧 Configuration

### Site Generator (`application.properties`)
```properties
server.port=8080
site.generator.output.path=output
spring.thymeleaf.cache=false
spring.servlet.multipart.max-file-size=10MB
```

### Site Builder (`application.properties`)
```properties
server.port=8081
site.builder.input.path=output
site.builder.build.path=build
```

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch: `git checkout -b feature/amazing-feature`
3. Commit your changes: `git commit -m 'Add amazing feature'`
4. Push to the branch: `git push origin feature/amazing-feature`
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👨‍💻 Author

**Lemrabott Toulba** - *Senior Java Developer*

---

## 🎯 Next Steps

- [ ] Add more section types (gallery, timeline, testimonials)
- [ ] Implement theme system
- [ ] Add image optimization
- [ ] Create web UI for site generation
- [ ] Add CI/CD pipeline
- [ ] Implement caching strategies
- [ ] Add internationalization support