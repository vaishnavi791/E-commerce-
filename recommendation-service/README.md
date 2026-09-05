# Recommendation Service

This local FastAPI service provides product recommendations for Spring Boot.

## Run

```powershell
cd recommendation-service
python -m venv .venv
.\.venv\Scripts\Activate.ps1
pip install -r requirements.txt
uvicorn main:app --reload --port 8000
```

Health check: `http://localhost:8000/health`

Spring Boot calls `POST /recommend` internally. React never calls this service directly and does not receive any service credentials.

The service uses TF-IDF and cosine similarity over each product's category, subcategory, brand, and description. Interaction weights are `VIEW=1`, `CART_ADD=2`, and `PURCHASE=3`. A user with no interaction history receives recent product IDs.
