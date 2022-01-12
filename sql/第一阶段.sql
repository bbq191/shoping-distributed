SELECT i.id                    AS itemId,
       i.item_name             AS itemName,
       i.sell_counts           AS sellCounts,
       ii.url                  AS imgUrl,
       tempSpec.price_discount AS price
FROM items i
         LEFT JOIN items_img ii ON i.id = ii.item_id
         LEFT JOIN (
    SELECT item_id,
           MIN(price_discount) AS price_discount
    FROM items_spec
    GROUP BY item_id) tempSpec ON i.id = tempSpec.item_id
WHERE ii.is_main = 1;

SELECT t_items.id                  as itemId,
       t_items.item_name           as itemName,
       t_items_img.url             as itemImgUrl,
       t_items_spec.id             as specId,
       t_items_spec.`name`         as specName,
       t_items_spec.price_discount as priceDiscount,
       t_items_spec.price_normal   as priceNormal
FROM items_spec t_items_spec
         LEFT JOIN
     items t_items
     ON
         t_items.id = t_items_spec.item_id
         LEFT JOIN
     items_img t_items_img
     on
         t_items_img.item_id = t_items.id
WHERE t_items_img.is_main = 1
  AND t_items_spec.id IN ('1', '3', '5');

select *
from user_address;