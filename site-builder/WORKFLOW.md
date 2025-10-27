This service takes the generated HTML files and builds a complete static site package (including sitemap, configuration, etc.), optionally serving it locally.

REST Endpoint:
GET /api/build?siteName=MyPortfolio


Output Folder:
build/
 └── MyPortfolio/
      ├── index.html
      ├── contact.html
      ├── sitemap.xml
      └── assets/