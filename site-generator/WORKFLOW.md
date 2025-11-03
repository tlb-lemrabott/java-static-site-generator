## ⚙️ How It Works

### 1️⃣ `site-generator` (JSON → HTML)

Reads a JSON file describing your site—pages, sections, and content—and uses Thymeleaf templates to generate static HTML files.

- Input: `site-description.json`
- Process: Parse JSON → Render Thymeleaf templates → Write files
- Output: Static site in `output/<siteName>/`

Example JSON (`site-description.json`):
```json
{
  "siteName": "MyPortfolio",
  "pages": [
    {
      "title": "Home",
      "slug": "index",
      "sections": [
        { "type": "hero", "heading": "Welcome!", "text": "I'm a software engineer." },
        { "type": "skills", "items": ["Java", "Spring Boot", "AWS"] }
      ]
    },
    {
      "title": "Contact",
      "slug": "contact",
      "sections": [
        { "type": "form", "fields": ["name", "email", "message"] }
      ]
    }
  ]
}
```

REST Endpoint:
```
POST /api/generate
```

Request (multipart/form-data):
- Field: `file`
- Value: uploaded JSON file (e.g., `site-description.json`)

Example using curl:
```bash
curl -X POST http://localhost:8080/api/generate \
  -F "file=@site-description.json"
```

Output folder structure:
```bash
output/
 └── MyPortfolio/
      ├── index.html
      ├── contact.html
      ├── assets/
      │    ├── styles.css
      │    └── script.js
      └── config.json
```
