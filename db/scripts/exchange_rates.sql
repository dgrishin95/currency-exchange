CREATE TABLE exchange_rates
(
    id               INTEGER PRIMARY KEY AUTOINCREMENT,
    basecurrencyid   INTEGER NOT NULL,
    targetcurrencyid INTEGER NOT NULL,
    rate             DECIMAL(10, 6),
    FOREIGN KEY (basecurrencyid) REFERENCES currencies (id),
    FOREIGN KEY (targetcurrencyid) REFERENCES currencies (id),
    UNIQUE (basecurrencyid, targetcurrencyid)
);

INSERT INTO exchange_rates (BaseCurrencyId, TargetCurrencyId, Rate)
VALUES (1, 2, 0.920000),   -- USD → EUR
       (2, 1, 1.087000),   -- EUR → USD
       (1, 3, 155.250000), -- USD → JPY
       (3, 1, 0.006440);