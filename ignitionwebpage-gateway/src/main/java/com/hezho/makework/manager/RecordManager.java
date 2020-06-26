package com.hezho.makework.manager;

import com.google.common.base.Preconditions;
import com.inductiveautomation.ignition.gateway.localdb.persistence.PersistenceInterface;
import com.inductiveautomation.ignition.gateway.localdb.persistence.PersistentRecord;
import com.inductiveautomation.ignition.gateway.localdb.persistence.RecordMeta;
import com.inductiveautomation.ignition.gateway.model.ExtensionPointType;
import com.inductiveautomation.ignition.gateway.web.models.KeyValue;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import simpleorm.dataset.SQuery;

@Slf4j
public class RecordManager {
    @NonNull
    private final PersistenceInterface persistence;

    public <T extends PersistentRecord> T getOnlyRecord(@NonNull RecordMeta<T> meta) {
        if (meta == null) {
            throw new NullPointerException("meta is marked non-null but is null");
        } else {
            List<T> records = this.getRecords(meta);
            Preconditions.checkState(records.size() == 1, "Expected one record. meta=[%s], size=[%s]", new Object[]{meta, records.size()});
            return (T)records.get(0);
        }
    }

    public <T extends PersistentRecord> List<T> getRecords(@NonNull RecordMeta<T> meta) {
        if (meta == null) {
            throw new NullPointerException("meta is marked non-null but is null");
        } else {
            SQuery<T> query = new SQuery(meta);
            return (List)Preconditions.checkNotNull(this.persistence.query(query), "Null response for meta. meta=[%s]", new Object[]{meta});
        }
    }

    public <T extends PersistentRecord> Optional<T> getRecordByPrimaryKeys(@NonNull RecordMeta<T> meta, @NonNull Object... primaryKeys) {
        if (meta == null) {
            throw new NullPointerException("meta is marked non-null but is null");
        } else if (primaryKeys == null) {
            throw new NullPointerException("primaryKeys is marked non-null but is null");
        } else {
            return Optional.ofNullable(this.persistence.find(meta, primaryKeys));
        }
    }

    public <T extends PersistentRecord> T createNew(@NonNull RecordMeta<T> meta) {
        if (meta == null) {
            throw new NullPointerException("meta is marked non-null but is null");
        } else {
            return (T)Preconditions.checkNotNull(this.persistence.createNew(meta), "Null response for meta. meta=[%s]", new Object[]{meta});
        }
    }

    public <T extends PersistentRecord> T createNewAndSave(@NonNull RecordMeta<T> meta, @NonNull Consumer<T> recordMutator) {
        if (meta == null) {
            throw new NullPointerException("meta is marked non-null but is null");
        } else if (recordMutator == null) {
            throw new NullPointerException("recordMutator is marked non-null but is null");
        } else {
            T record = this.createNew(meta);
            recordMutator.accept(record);
            this.persistence.save(record);

            try {
                this.persistence.notifyRecordAdded(record);
                return record;
            } catch (Exception var5) {
                //throw new IllegalStateException(String.format("Notifications failed. record=[%s]", RecordUtil.toJSON(record)));
                throw new IllegalStateException();
            }
        }
    }

    public void delete(@NonNull PersistentRecord record) {
        if (record == null) {
            throw new NullPointerException("record is marked non-null but is null");
        } else {
            KeyValue deletedKeyValue = new KeyValue(record);
            record.deleteRecord();
            this.persistence.save(record);

            try {
                this.persistence.notifyRecordDeleted(record.getMeta(), deletedKeyValue);
            } catch (Exception var4) {
                throw new IllegalStateException(String.format("Notifications failed. deletedKeyValue=[%s]", deletedKeyValue), var4);
            }
        }
    }

    public void update(@NonNull PersistentRecord record) {
        if (record == null) {
            throw new NullPointerException("record is marked non-null but is null");
        } else {
            this.persistence.save(record);

            try {
                log.debug("HI, I AM UPDATED!");
                this.persistence.notifyRecordUpdated(record);
            } catch (Exception var3) {
                //throw new IllegalStateException(String.format("Notifications failed. record=[%s]", RecordUtil.toJSON(record)));
                throw new IllegalStateException();
            }
        }
    }

    public void updateWithoutNotify(@NonNull PersistentRecord record) {
        if (record == null) {
            throw new NullPointerException("record is marked non-null but is null");
        } else {
            this.persistence.save(record);
        }
    }

    public <P extends PersistentRecord, E extends PersistentRecord> void createNewAndSave(@NonNull RecordMeta<P> primaryMeta, @NonNull RecordMeta<E> extensionMeta, @NonNull ExtensionPointType extensionPointType, @NonNull BiConsumer<ExtensionPointType, P> primaryMutator, @NonNull BiConsumer<ExtensionPointType, E> extensionMutator) {
        if (primaryMeta == null) {
            throw new NullPointerException("primaryMeta is marked non-null but is null");
        } else if (extensionMeta == null) {
            throw new NullPointerException("extensionMeta is marked non-null but is null");
        } else if (extensionPointType == null) {
            throw new NullPointerException("extensionPointType is marked non-null but is null");
        } else if (primaryMutator == null) {
            throw new NullPointerException("primaryMutator is marked non-null but is null");
        } else if (extensionMutator == null) {
            throw new NullPointerException("extensionMutator is marked non-null but is null");
        } else {
            Preconditions.checkArgument(Objects.equals(extensionPointType.getSettingsRecordType(), extensionMeta));
            P primaryRecord = this.createNew(primaryMeta);
            E extensionRecord = this.createNew(extensionMeta);
            primaryMutator.accept(extensionPointType, primaryRecord);
            extensionMutator.accept(extensionPointType, extensionRecord);
            this.persistence.save(primaryRecord);
            extensionRecord.setReference(extensionPointType.getSettingsRecordForeignKey(), primaryRecord);
            this.persistence.save(extensionRecord);

            try {
                this.persistence.notifyRecordAdded(primaryRecord);
            } catch (Exception var9) {
                //throw new IllegalStateException(String.format("Notifications failed. record=[%s]", RecordUtil.toJSON(primaryRecord)));
                throw new IllegalStateException();
            }
        }
    }

    public RecordManager(@NonNull PersistenceInterface persistence) {
        if (persistence == null) {
            throw new NullPointerException("persistence is marked non-null but is null");
        } else {
            this.persistence = persistence;
        }
    }
}


