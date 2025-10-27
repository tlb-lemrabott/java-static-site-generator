## ⚙️ How It Works

### 1️⃣ `site-generator` (JSON → HTML)

This service reads a JSON file that describes your website structure — pages, sections, content — and uses **Thymeleaf templates** to generate static HTML files.

**Example JSON (`site-description.json`):**
```json
{
  "siteName": "MyPortfolio",
  "pages": [
    {
      "title": "Home",
      "slug": "index",
      "sections": [
        {"type": "hero", "heading": "Welcome!", "text": "I'm a software engineer."},
        {"type": "skills", "items": ["Java", "Spring Boot", "AWS"]}
      ]
    },
    {
      "title": "Contact",
      "slug": "contact",
      "sections": [
        {"type": "form", "fields": ["name", "email", "message"]}
      ]
    }
  ]
}

REST Endpoint:
POST /api/generate

Request:
Upload your JSON file via form-data:
file = site-description.json

Output Folder:

output/
 └── MyPortfolio/
      ├── index.html
      ├── contact.html
      ├── assets/
      │    ├── styles.css
      │    └── script.js
      └── config.json