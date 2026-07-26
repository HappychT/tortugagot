import json
from datetime import datetime
from sqlalchemy import Column, String, Text, DateTime, Integer
from database import Base

class MapConfig(Base):
    """
    Key-Value store for flexible, high-performance map data storage.
    Keys: 'overrides', 'territories', 'terrain_zones', 'terrain_multipliers'
    In PostgreSQL/SQLite, stores JSON string in value_json.
    """
    __tablename__ = "map_config"

    key = Column(String(64), primary_key=True, index=True)
    value_json = Column(Text, nullable=False, default="{}")
    updated_at = Column(DateTime, default=datetime.utcnow, onupdate=datetime.utcnow)

    def get_data(self):
        try:
            return json.loads(self.value_json)
        except Exception:
            return {}

    def set_data(self, data):
        self.value_json = json.dumps(data, ensure_ascii=False)


class AdminAuditLog(Base):
    """
    Audit log for security and monitoring of admin actions.
    """
    __tablename__ = "admin_audit_log"

    id = Column(Integer, primary_key=True, index=True)
    action = Column(String(128), nullable=False)
    details = Column(Text, nullable=True)
    ip_address = Column(String(64), nullable=True)
    timestamp = Column(DateTime, default=datetime.utcnow)
