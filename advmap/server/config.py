import os
from dotenv import load_dotenv
from pydantic_settings import BaseSettings

load_dotenv()

class Settings(BaseSettings):
    # Database configuration: defaults to SQLite for out-of-the-box local development,
    # set DATABASE_URL="postgresql://user:password@localhost:5432/tortuga" in production!
    DATABASE_URL: str = os.getenv("DATABASE_URL", "sqlite:///./map_database.db")
    
    # Security & Auth
    SECRET_KEY: str = os.getenv("SECRET_KEY", "tortuga_got_map_secret_key_2026")
    ALGORITHM: str = "HS256"
    ACCESS_TOKEN_EXPIRE_HOURS: int = 72
    
    # Default admin password SHA-256 hash (default is hash of "123": a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3)
    ADMIN_PASSWORD_HASH: str = os.getenv(
        "ADMIN_PASSWORD_HASH",
        "a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3"
    )
    
    # In-memory caching for zero DB load under high player concurrency
    MAP_DATA_CACHE_TTL: int = int(os.getenv("MAP_DATA_CACHE_TTL", "30"))

    class Config:
        env_file = ".env"

settings = Settings()
